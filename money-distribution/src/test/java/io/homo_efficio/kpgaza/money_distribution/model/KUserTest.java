package io.homo_efficio.kpgaza.money_distribution.model;

import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatUser;
import io.homo_efficio.kpgaza.money_distribution.domain.model.KUser;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatUserRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.KUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class KUserTest {

    @Autowired
    private KUserRepository kUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @DisplayName("대화방에 입장하면 KUser의 chatUsers와 ChatRoom의 chatUsers에 입장 데이터가 추가돼야 한다.")
    @Test
    void chatRoomEntranceTest() {
        // given
        KUser kUser1 = kUserRepository.save(new KUser(null, "K사용자1"));
        UUID chatRoomId = UUID.randomUUID();
        ChatRoom chatRoom1 = chatRoomRepository.save(new ChatRoom(chatRoomId, "대화방1", kUser1));

        // when
        ChatUser chatUser1 = chatUserRepository.save(kUser1.enterChatRoom(chatRoom1));

        // then
        assertThat(kUser1.getChatUsers()).contains(chatUser1);
        assertThat(chatRoom1.getChatUsers()).contains(chatUser1);
    }
}
