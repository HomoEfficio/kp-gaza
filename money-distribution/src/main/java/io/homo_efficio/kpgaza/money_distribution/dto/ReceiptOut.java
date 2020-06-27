package io.homo_efficio.kpgaza.money_distribution.dto;

import io.homo_efficio.kpgaza.money_distribution.domain.model.Receipt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReceiptOut {

    private Long id;
    private Long receiverId;
    private Integer amount;


    public static ReceiptOut from(Receipt receipt) {
        return new ReceiptOut(
                receipt.getId(),
                receipt.getReceiverId(),
                receipt.getAmount()
        );
    }
}
