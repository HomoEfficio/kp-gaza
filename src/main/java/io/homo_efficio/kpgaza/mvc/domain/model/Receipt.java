package io.homo_efficio.kpgaza.mvc.domain.model;

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

    @NotNull
    private Long receiverId;

    @NotNull
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    private Distribution distribution;

    @Version
    private Long version;
}
