package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.dto.ReceiptIn;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
public interface ReceiptService {

    ReceiptOut create(ReceiptIn receiptIn);
}
