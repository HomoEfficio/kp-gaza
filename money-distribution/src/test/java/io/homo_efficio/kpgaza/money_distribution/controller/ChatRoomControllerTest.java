package io.homo_efficio.kpgaza.money_distribution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.util.ApplicationContextTestUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ChatRoomControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    private JacksonTester<ChatRoomIn> chatRoomInJackson;

    @BeforeEach
    public void beforeEach() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @ParameterizedTest(name = "방이름: {0}, 방장id: {1}인 대화방을 생성한다.")
    @MethodSource("chatRooms")
    @Sql(scripts = "classpath:sql/h2/init-kusers.sql")
    void create(String name, Long ownerId) throws Exception {
        mvc.perform(post("/chat-rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(chatRoomInJackson.write(new ChatRoomIn(null, name, ownerId)).getJson()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(name))
            .andExpect(jsonPath("ownerId").value(ownerId));
    }

    private static Stream<Arguments> chatRooms() {
        return Stream.of(
                Arguments.of("방11", 1L),
                Arguments.of("방12", 1L),
                Arguments.of("방21", 2L)
        );
    }

    @ParameterizedTest(name = "방id: [{0}]에 사용자id: [{1}]이 입장한다.")
    @MethodSource("chatUsers")
    @Sql(scripts = "classpath:sql/h2/init-chatrooms.sql")
    void createChatUser(UUID chatRoomId, Long chatterId) throws Exception {
        mvc.perform(post("/chat-rooms/" + chatRoomId + "/chatters/" + chatterId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("chatRoomId").value(chatRoomId.toString()))
            .andExpect(jsonPath("chatterId").value(chatterId));
    }

    private static Stream<Arguments> chatUsers() {
        return Stream.of(
                Arguments.of("4cf55070-10ae-4097-afcf-d61a25cfd233", 4L),
                Arguments.of("4cf55070-10ae-4097-afcf-d61a25cfd233", 6L),
                Arguments.of("4cf55070-10ae-4097-afcf-d61a25cfd233", 8L),
                Arguments.of("74b45ce5-0939-4eff-bcd3-8a6277066252", 3L),
                Arguments.of("74b45ce5-0939-4eff-bcd3-8a6277066252", 5L),
                Arguments.of("b7dd1bf7-bf20-48f8-98ff-558f04faa35f", 7L),
                Arguments.of("b7dd1bf7-bf20-48f8-98ff-558f04faa35f", 9L),
                Arguments.of("c70f1bbc-787d-4706-aac1-54cc609997ca", 1L),
                Arguments.of("c70f1bbc-787d-4706-aac1-54cc609997ca", 3L),
                Arguments.of("c70f1bbc-787d-4706-aac1-54cc609997ca", 5L),
                Arguments.of("cf12c71e-e81e-4c41-9750-187f670b0847", 2L),
                Arguments.of("cf12c71e-e81e-4c41-9750-187f670b0847", 4L),
                Arguments.of("cf12c71e-e81e-4c41-9750-187f670b0847", 6L),
                Arguments.of("cf12c71e-e81e-4c41-9750-187f670b0847", 8L),
                Arguments.of("f5addd51-2d45-47a2-8d15-d95ece9b21b8", 1L),
                Arguments.of("f5addd51-2d45-47a2-8d15-d95ece9b21b8", 3L),
                Arguments.of("396d7ac2-aa65-4f56-8ec1-ccc8b88b332c", 2L),
                Arguments.of("396d7ac2-aa65-4f56-8ec1-ccc8b88b332c", 4L),
                Arguments.of("396d7ac2-aa65-4f56-8ec1-ccc8b88b332c", 6L),
                Arguments.of("23662be4-2230-42c9-a93b-a58049fbd414", 1L),
                Arguments.of("23662be4-2230-42c9-a93b-a58049fbd414", 2L),
                Arguments.of("23662be4-2230-42c9-a93b-a58049fbd414", 3L)
//                Arguments.of("c2cef0fb-7108-4f5e-92e3-9b3011f77fbf", ), 방장 9L만 있는 방
        );
    }

    @DisplayName("대화방 목록을 조회한다.")
    @Test
    @Sql(scripts = "classpath:sql/h2/init-chatrooms.sql")
    void listChatRooms() throws Exception {
        mvc.perform(get("/chat-rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isArray())
                .andExpect(jsonPath("content").isNotEmpty())
                .andExpect(jsonPath("totalElements").value(9))
                .andExpect(jsonPath("totalPages").value(3))
                .andExpect(jsonPath("content[0].id").value("4cf55070-10ae-4097-afcf-d61a25cfd233"))
                .andExpect(jsonPath("content[0].name").value("방1"))
                .andExpect(jsonPath("content[0].ownerId").value(1L))
                .andExpect(jsonPath("content[0].ownerName").value("ㅋㅋㅇ"))
        ;
    }
}
