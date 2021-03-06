package io.homo_efficio.kpgaza.money_distribution.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistributionIn {

    @NotEmpty
    private Long distributorId;
    @NotEmpty
    private UUID chatRoomId;
    @NotNull
    private Integer amount;
    @NotNull
    private Integer targets;

    public DistributionIn(Integer amount, Integer targets) {
        this.amount = amount;
        this.targets = targets;
    }
}
