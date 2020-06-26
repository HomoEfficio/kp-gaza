package io.homo_efficio.kpgaza.mvc.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Distribution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 3)
    private String token;

    @NotNull
    private Integer amount;

    @NotNull
    private Integer targets;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distributor_id")
    private KUser distributor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "distribution", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Receipt> receipts = new HashSet<>();


    public Distribution(Long id, KUser distributor, ChatRoom chatRoom, Integer amount, Integer targets) {
        this.id = null;
        this.token = generateToken();
        this.distributor = distributor;
        this.chatRoom = chatRoom;
        this.amount = amount;
        this.targets = targets;
    }

    public Distribution(Long id, String token, KUser distributor, ChatRoom chatRoom, Integer amount, Integer targets) {
        this(id, distributor, chatRoom, amount, targets);
        this.token = token;
    }

    public Integer getReceivedAmount() {
        Integer receivedAmount = 0;
        for (Receipt receipt : receipts) {
            receivedAmount += receipt.getAmount();
        }
        return receivedAmount;
    }

    private String generateToken() {
        return RandomStringUtils.random(3, true, true);
    }
}
