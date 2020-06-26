package io.homo_efficio.kpgaza.mvc.domain.model;

import lombok.*;

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


    public void receivedBy(Long receiverId) {
        this.receiverId = receiverId;
        this.status = Status.CLOSED;
    }

    @Version
    private Long version;
}
