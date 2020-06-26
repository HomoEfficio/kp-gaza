package io.homo_efficio.kpgaza.mvc.dto;

import lombok.*;

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
}
