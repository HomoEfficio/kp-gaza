package io.homo_efficio.kpgaza.money_distribution.service;

import io.homo_efficio.kpgaza.money_distribution.dto.ReceiptIn;
import io.homo_efficio.kpgaza.money_distribution.dto.ReceiptOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
public interface ReceiptService {

    ReceiptOut create(ReceiptIn receiptIn);
}
