package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.dto.KUserIn;
import io.homo_efficio.kpgaza.mvc.dto.KUserOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface KUserService {

    KUserOut create(KUserIn kUserIn);
}
