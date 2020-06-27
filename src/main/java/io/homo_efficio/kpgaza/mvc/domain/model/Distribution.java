package io.homo_efficio.kpgaza.mvc.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Table(indexes = {
        @Index(name = "IDX_DISTRIBUTION__TOKEN", columnList = "token", unique = true)
})
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
    private List<Receipt> receipts = new ArrayList<>();


    public Distribution(Long id, KUser distributor, ChatRoom chatRoom, Integer amount, Integer targets) {
        this.id = null;
        this.token = generateToken();
        this.distributor = distributor;
        this.chatRoom = chatRoom;
        this.amount = amount;
        this.targets = targets;
        initReceipts(targets);
    }

    private void initReceipts(Integer targets) {
        List<Integer> amountsPerCapita = getAmountsPerCapita();
        for (int i = 0; i < targets; i++) {
            receipts.add(new Receipt(null, null, amountsPerCapita.get(i), this, Receipt.Status.OPEN));
        }
    }

    public Distribution(Long id, String token, KUser distributor, ChatRoom chatRoom, Integer amount, Integer targets) {
        this(id, distributor, chatRoom, amount, targets);
        this.token = token;
    }

    public Integer getReceivedAmount() {
        Integer receivedAmount = 0;
        for (Receipt receipt : receipts) {
            if (receipt.isReceived()) {
                receivedAmount += receipt.getAmount();
            }
        }
        return receivedAmount;
    }

    private String generateToken() {
        return RandomStringUtils.random(3, true, true);
    }

    private List<Integer> getAmountsPerCapita() {
        List<Integer> result = new ArrayList<>(targets);
        int residue = amount % targets;
        if (residue == 0) {
            for (int i = 0; i < targets; i++) {
                result.add(amount / targets);
            }
            return result;
        } else {
            int perCapita = amount / targets;
            result.add(perCapita + residue);
            for (int i = 1; i < targets; i++) {
                result.add(perCapita);
            }
            return result;
        }
    }

    public List<Receipt> getClosedReceipts() {
        return receipts.stream().filter(Receipt::isReceived).collect(Collectors.toList());
    }
}
