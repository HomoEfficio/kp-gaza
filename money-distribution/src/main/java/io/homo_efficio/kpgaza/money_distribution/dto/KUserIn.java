package io.homo_efficio.kpgaza.money_distribution.dto;

import io.homo_efficio.kpgaza.money_distribution.domain.model.KUser;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KUserIn {

    private Long id;

    @NotEmpty
    @Min(value = 2, message = "사용자 이름은 2자 이상이어야 합니다.")
    @Max(value = 20, message = "사용자 이름은 10자 이내여야 합니다.")
    private String name;

    public KUser mapToEntity() {
        return new KUser(id, name);
    }
}
