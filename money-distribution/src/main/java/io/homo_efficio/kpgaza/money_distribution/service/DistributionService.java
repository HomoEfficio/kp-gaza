package io.homo_efficio.kpgaza.money_distribution.service;

import io.homo_efficio.kpgaza.money_distribution.dto.DistributionIn;
import io.homo_efficio.kpgaza.money_distribution.dto.DistributionOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface DistributionService {

    DistributionOut createDistribution(DistributionIn distributionIn);
    DistributionOut findByToken(String token, Long requesterId);
}
