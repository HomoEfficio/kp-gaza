package io.homo_efficio.kpgaza.money_distribution.domain.model;

import io.homo_efficio.kpgaza.money_distribution._common.exception.InvalidDistributionException;
import io.homo_efficio.kpgaza.money_distribution._common.exception.InvalidReceiptException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Table(name = "k_user")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class KUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "chatter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatUser> chatUsers = new HashSet<>();


    public KUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatRoom createChatRoom(String roomName) {
        return new ChatRoom(UUID.randomUUID(), roomName, this);
    }

    public ChatUser enterChatRoom(ChatRoom chatRoom) {
        return new ChatUser(null, chatRoom, this);
    }

    public Distribution distributeMoney(ChatRoom chatRoom, Integer amount, Integer targets) {
        if (!chatRoom.containsUser(this)) {
            throw new InvalidDistributionException(
                    String.format("대화방 [%s]에 사용자 [%d]가 존재하지 않아 뿌리기를 할 수 없습니다.",
                            chatRoom.getId(), id));
        }
        return new Distribution(null, this, chatRoom, amount, targets);
    }

    public Receipt receiveMoney(Distribution distribution) {
        Receipt receipt = distribution.getReceipts().stream()
                .filter(r -> r.getStatus().equals(Receipt.Status.OPEN))
                .findFirst()
                .orElseThrow(() -> new InvalidReceiptException(
                        String.format("토큰 [%s]로 뿌려진 머니가 이미 모두 수령됐습니다.", distribution.getToken())));

        boolean multipleReceiptTrial = distribution.getReceipts().stream()
                .filter(r -> r.getStatus().equals(Receipt.Status.CLOSED))
                .anyMatch(r -> r.getReceiverId().equals(this.id));
        if (multipleReceiptTrial)
            throw new InvalidReceiptException("동일한 뿌리기에서 중복 수령은 허용되지 않습니다.");

        if (distribution.getDistributor().equals(this))
            throw new InvalidReceiptException("자기가 뿌린 머니는 수령할 수 없습니다.");

        if (!distribution.getChatRoom().containsUser(this)) {
            throw new InvalidReceiptException("참여하지 않은 대화방에 있는 뿌리기는 수령할 수 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (distribution.getCreatedDateTime().isBefore(now.minus(10, ChronoUnit.MINUTES)))
            throw new InvalidReceiptException("10분 이상 경과된 뿌리기는 수령할 수 없습니다.");

        receipt.receivedBy(this.id);
        return receipt;
    }

    public Distribution showDistribution(Distribution distribution) {
        if (!distribution.getDistributor().equals(this)) {
            throw new InvalidDistributionException("자기가 뿌린 뿌리기 정보만 조회할 수 있습니다.");
        }

        if (!distribution.isRetrievalAvailable()) {
            throw new InvalidDistributionException("뿌린 지 7일 이내 뿌리기 정보만 조회할 수 있습니다.");
        }

        return distribution;
    }
}
