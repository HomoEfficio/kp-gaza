package io.homo_efficio.kpgaza.money_distribution.dto;

import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUserOut {

    private final Long id;
    private final UUID chatRoomId;
    private final Long chatterId;

    public static ChatUserOut from(ChatUser chatUser) {
        return new ChatUserOut(
                chatUser.getId(),
                chatUser.getChatRoom().getId(),
                chatUser.getChatter().getId()
        );
    }
}
