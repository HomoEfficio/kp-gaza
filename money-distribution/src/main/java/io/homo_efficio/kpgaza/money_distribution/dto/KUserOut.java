package io.homo_efficio.kpgaza.money_distribution.dto;

import io.homo_efficio.kpgaza.money_distribution.domain.model.KUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KUserOut {

    private final Long id;
    private final String name;

    public static KUserOut from(KUser kUser) {
        return new KUserOut(
                kUser.getId(),
                kUser.getName()
        );
    }
}
