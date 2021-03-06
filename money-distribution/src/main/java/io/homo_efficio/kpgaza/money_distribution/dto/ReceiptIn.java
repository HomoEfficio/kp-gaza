package io.homo_efficio.kpgaza.money_distribution.dto;

import lombok.*;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiptIn {

    private String token;
    private Long receiverId;
    private UUID chatRoomId;

    public ReceiptIn(String token) {
        this.token = token;
    }
}
