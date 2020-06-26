package io.homo_efficio.kpgaza.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.NestedServletException;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
@SpringBootTest
@Transactional
public class ReceiptControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    private JacksonTester<ReceiptIn> receiptInJackson;

    @BeforeEach
    public void beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @ParameterizedTest(name = "대화방 멤버 [{0}]가 대화방 [{1}]에서 토큰 [{2}]을 사용해서 뿌려진 머니를 수령하면, 수령 금액 [{3}]을 반환한다.")
    @MethodSource("receipts")
    @Sql(scripts = "classpath:init-distributions.sql")
    void createReceipt(Long chatterId, UUID chatRoomId, String token, Integer amount) throws Exception {
        mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", chatterId)
                .header("X-ROOM-ID", chatRoomId)
                .content(receiptInJackson.write(new ReceiptIn(token)).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(amount));
    }

    private static Stream<Arguments> receipts() {
        return Stream.of(
                Arguments.of(2L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a11", 50),
                Arguments.of(3L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a11", 50),

                Arguments.of(1L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a21", 100),
                Arguments.of(3L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a21", 100),
                Arguments.of(4L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a21", 100),
                Arguments.of(5L, "4cf55070-10ae-4097-afcf-d61a25cfd233", "a21", 100),

                Arguments.of(6L, "b7dd1bf7-bf20-48f8-98ff-558f04faa35f", "c41", 300)
        );
    }

    @DisplayName("1/N 이 딱 떨어지지 않을 때는 첫번쨰 수령자가 나머지를 포함한 금액을 수령한다.")
    @Sql(scripts = "classpath:init-distributions-inequal-amounts.sql")
    @Test
    void createReceiptWithInEqualAmounts() throws Exception {
        mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 2)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(34));

        mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 3)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(33));

        mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 4)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(33));
    }

    @DisplayName("동일한 뿌리기에서 한 사용자가 중복 수령 시도하면 예외가 발생한다.")
    @Test
    @Sql(scripts = "classpath:init-distributions.sql")
    void receiveMultipleTimesInSameDistributionProhibited() throws Exception {
        mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 2)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("amount").value(50));

        assertThrows(NestedServletException.class, () -> mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 2)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson())));
    }

    @DisplayName("자기가 뿌린 뿌리기에서 수령 시도하면 예외가 발생한다.")
    @Test
    @Sql(scripts = "classpath:init-distributions.sql")
    void selfReceiptProhibited() {
        assertThrows(NestedServletException.class, () -> mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 1)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson())));
    }

    @DisplayName("다른 대화방의 사용자가 다른 방의 뿌리기에서 수령 시도하면 예외가 발생한다.")
    @Test
    @Sql(scripts = "classpath:init-distributions.sql")
    void differentChatRoomReceiptProhibited() {
        assertThrows(NestedServletException.class, () -> mvc.perform(post("/receipts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 6)
                .header("X-ROOM-ID", "4cf55070-10ae-4097-afcf-d61a25cfd233")
                .content(receiptInJackson.write(new ReceiptIn("a11")).getJson())));
    }
}
