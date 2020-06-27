package io.homo_efficio.kpgaza.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.kpgaza.mvc.dto.DistributionIn;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@SpringBootTest
@Transactional
class DistributionControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    private JacksonTester<DistributionIn> distributionInJackson;

    @BeforeEach
    public void beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @ParameterizedTest(name = "대화방 멤버 [{0}]가 대화방 [{1}]에서 머니 [{2}]를 [{3}]명에게 뿌리면, 뿌리기 요청건에 대한 토큰을 반환한다.")
    @MethodSource("distributions")
    @Sql(scripts = "classpath:init-chatusers.sql")
    void createDistribution(Long chatterId, UUID chatRoomId, int money, int targets) throws Exception {
        mvc.perform(post("/distributions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", chatterId)
                .header("X-ROOM-ID", chatRoomId)
                .content(distributionInJackson.write(new DistributionIn(money, targets)).getJson()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").exists())
            .andExpect(jsonPath("token").isNotEmpty())
            .andExpect(jsonPath("token").isString())
            .andExpect(jsonPath("token").value(hasLength(3)));
    }

    private static Stream<Arguments> distributions() {
        return Stream.of(
                Arguments.of(1L, "4cf55070-10ae-4097-afcf-d61a25cfd233", 300, 2),
                Arguments.of(2L, "4cf55070-10ae-4097-afcf-d61a25cfd233", 100, 4),
                Arguments.of(4L, "b7dd1bf7-bf20-48f8-98ff-558f04faa35f", 200, 1)
        );
    }

    @DisplayName("뿌린 사용자 가 토큰 으로 뿌리기 를 조회하면, " +
            "뿌린 시각, 뿌린 금액, 받기 완료 총금액, 받기 완료 목록 을 반환한다 - 전액 다 수령된 케이스")
    @Test
    @Sql(scripts = "classpath:init-receipts.sql")
    void findDistributionFullyReceived() throws Exception {
        mvc.perform(get("/distributions?token=" + "a11")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("distributedAt").exists())
                .andExpect(jsonPath("distributedAt").isNotEmpty())
                .andExpect(jsonPath("amount").value(100))
                .andExpect(jsonPath("receivedAmount").value(100))
                .andExpect(jsonPath("receipts").isArray())
                .andExpect(jsonPath("receipts", hasSize(2)))
                .andExpect(jsonPath("receipts[0].amount").value(50))
                .andExpect(jsonPath("receipts[0].receiverId").value(2L))
                .andExpect(jsonPath("receipts[1].amount").value(50))
                .andExpect(jsonPath("receipts[1].receiverId").value(3L))
        ;
    }

    @DisplayName("뿌린 사용자 가 토큰 으로 뿌리기 를 조회하면, " +
            "뿌린 시각, 뿌린 금액, 받기 완료 총금액, 받기 완료 목록 을 반환한다 - 일부 수령된 케이스")
    @Test
    @Sql(scripts = "classpath:init-receipts.sql")
    void findDistributionPartiallyReceived() throws Exception {
        mvc.perform(get("/distributions?token=" + "a21")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("distributedAt").exists())
                .andExpect(jsonPath("distributedAt").isNotEmpty())
                .andExpect(jsonPath("amount").value(400))
                .andExpect(jsonPath("receivedAmount").value(200))
                .andExpect(jsonPath("receipts").isArray())
                .andExpect(jsonPath("receipts", hasSize(2)))
                .andExpect(jsonPath("receipts[0].amount").value(100))
                .andExpect(jsonPath("receipts[0].receiverId").value(5L))
                .andExpect(jsonPath("receipts[1].amount").value(100))
                .andExpect(jsonPath("receipts[1].receiverId").value(3L))
        ;
    }

    @DisplayName("뿌린 사용자 가 토큰 으로 뿌리기 를 조회하면, " +
            "뿌린 시각, 뿌린 금액, 받기 완료 총금액, 받기 완료 목록 을 반환한다 - 수령 안 된 케이스")
    @Test
    @Sql(scripts = "classpath:init-receipts.sql")
    void findDistributionNotReceived() throws Exception {
        mvc.perform(get("/distributions?token=" + "c41")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("distributedAt").exists())
                .andExpect(jsonPath("distributedAt").isNotEmpty())
                .andExpect(jsonPath("amount").value(300))
                .andExpect(jsonPath("receivedAmount").value(0))
                .andExpect(jsonPath("receipts").isArray())
                .andExpect(jsonPath("receipts").isEmpty())
        ;
    }

    @ParameterizedTest(name = "본인이 아니면 뿌리기 정보 조회는 실패한다.")
    @MethodSource("unauthorizedRequesters")
    @Sql(scripts = "classpath:init-receipts.sql")
    void onlyDistributorCanSeeHisDistribution(String token, Long requesterId) throws Exception {
        mvc.perform(get("/distributions?token=" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", requesterId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("자기가 뿌린 뿌리기 정보만 조회할 수 있습니다."))
        ;
    }

    private static Stream<Arguments> unauthorizedRequesters() {
        return Stream.of(
                Arguments.of("a11", 2L),
                Arguments.of("a21", 3L),
                Arguments.of("c41", 9L)
        );
    }

    @DisplayName("7일 지난 뿌리기 정보 조회는 실패한다.")
    @Test
    @Sql(scripts = "classpath:init-distributions-expired.sql")
    void findDistributionAfter7DaysFails() throws Exception {
        mvc.perform(get("/distributions?token=" + "a11")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", 1L))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("뿌린 지 7일 이내 뿌리기 정보만 조회할 수 있습니다."))
        ;
    }
}
