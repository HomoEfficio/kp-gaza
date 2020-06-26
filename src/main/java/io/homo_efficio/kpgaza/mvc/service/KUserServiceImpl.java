package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.domain.repository.KUserRepository;
import io.homo_efficio.kpgaza.mvc.dto.KUserIn;
import io.homo_efficio.kpgaza.mvc.dto.KUserOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Service
@Transactional
@RequiredArgsConstructor
public class KUserServiceImpl implements KUserService {

    private final KUserRepository kUserRepository;

    @Override
    public KUserOut create(KUserIn kUserIn) {
        return KUserOut.from(kUserRepository.save(kUserIn.mapToEntity()));
    }
}
