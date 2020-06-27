package io.homo_efficio.kpgaza.money_distribution.service;

import io.homo_efficio.kpgaza.money_distribution.dto.KUserIn;
import io.homo_efficio.kpgaza.money_distribution.dto.KUserOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface KUserService {

    KUserOut create(KUserIn kUserIn);
}
