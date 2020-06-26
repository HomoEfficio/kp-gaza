package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.domain.model.Distribution;
import io.homo_efficio.kpgaza.mvc.dto.DistributionOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface DistributionService {

    DistributionOut createDistribution(Distribution distribution);
}