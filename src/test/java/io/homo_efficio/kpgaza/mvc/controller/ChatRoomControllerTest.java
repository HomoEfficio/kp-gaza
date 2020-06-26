package io.homo_efficio.kpgaza.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.homo_efficio.kpgaza.mvc.dto.ChatRoomIn;
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

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql(scripts = "classpath:init-kusers.sql")
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
}
