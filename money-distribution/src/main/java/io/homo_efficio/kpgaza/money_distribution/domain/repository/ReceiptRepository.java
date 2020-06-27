package io.homo_efficio.kpgaza.money_distribution.domain.repository;

import io.homo_efficio.kpgaza.money_distribution.domain.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
