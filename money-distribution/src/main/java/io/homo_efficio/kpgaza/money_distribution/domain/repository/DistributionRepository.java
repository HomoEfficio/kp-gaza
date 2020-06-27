package io.homo_efficio.kpgaza.money_distribution.domain.repository;

import io.homo_efficio.kpgaza.money_distribution.domain.model.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    Optional<Distribution> findByToken(String token);
}
