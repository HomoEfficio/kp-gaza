package io.homo_efficio.kpgaza.money_distribution.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Receipt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverId;

    @NotNull
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    private Distribution distribution;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        OPEN, CLOSED
    }

    @Version
    private Long version;

    public Receipt(Long id, Long receiverId, Integer amount, Distribution distribution, Status status) {
        this.id = id;
        this.receiverId = receiverId;
        this.amount = amount;
        this.distribution = distribution;
        this.status = status;
    }

    public void receivedBy(Long receiverId) {
        this.receiverId = receiverId;
        this.status = Status.CLOSED;
    }

    public boolean isReceived() {
        return this.status.equals(Status.CLOSED);
    }
}
