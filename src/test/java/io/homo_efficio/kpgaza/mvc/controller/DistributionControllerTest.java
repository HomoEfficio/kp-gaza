package io.homo_efficio.kpgaza.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.kpgaza.mvc.dto.DistributionIn;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasLength;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
