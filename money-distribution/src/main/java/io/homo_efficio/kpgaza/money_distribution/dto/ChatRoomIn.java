package io.homo_efficio.kpgaza.money_distribution.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoomIn {

    private UUID id;

    @NotEmpty
    @Min(value = 2, message = "대화방 이름은 2자 이상이어야 합니다.")
    @Max(value = 30, message = "대화방 이름은 30자 이내여야 합니다.")
    private String name;

    @NotNull
    private Long ownerId;
}
