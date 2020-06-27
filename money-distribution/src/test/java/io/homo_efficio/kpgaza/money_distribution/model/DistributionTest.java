package io.homo_efficio.kpgaza.money_distribution.model;

import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatUser;
import io.homo_efficio.kpgaza.money_distribution.domain.model.Distribution;
import io.homo_efficio.kpgaza.money_distribution.domain.model.KUser;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatUserRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.DistributionRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.KUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DistributionTest {

    @Autowired
    private KUserRepository kUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private DistributionRepository distributionRepository;

    @DisplayName("뿌리기가 생성되면 수령도 distributorId가 null인채로 생성된다.")
    @Test
    void createDistributionAndReceipts() {
        // given
        KUser kUser1 = kUserRepository.save(new KUser(null, "K사용자1"));
        UUID chatRoomId = UUID.randomUUID();
        ChatRoom chatRoom1 = chatRoomRepository.save(new ChatRoom(chatRoomId, "대화방1", kUser1));
        ChatUser chatUser1 = chatUserRepository.save(kUser1.enterChatRoom(chatRoom1));

        // when
        Distribution distribution = distributionRepository.save(kUser1.distributeMoney(chatRoom1, 20, 3));

        // then
        assertThat(distribution.getReceipts()).hasSize(3);
        assertThat(distribution.getReceipts().get(0).getReceiverId()).isNull();
        assertThat(distribution.getReceipts().get(0).getAmount()).isEqualTo(8);
        assertThat(distribution.getReceipts().get(1).getReceiverId()).isNull();
        assertThat(distribution.getReceipts().get(1).getAmount()).isEqualTo(6);
        assertThat(distribution.getReceipts().get(2).getReceiverId()).isNull();
        assertThat(distribution.getReceipts().get(2).getAmount()).isEqualTo(6);
    }
}
