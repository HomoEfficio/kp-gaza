package io.homo_efficio.kpgaza.mvc.dto;

import io.homo_efficio.kpgaza.mvc.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.mvc.domain.model.KUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomOut {

    private final String id;
    private final String name;
    private final Long ownerId;
    private final String ownerName;

    public static ChatRoomOut from(ChatRoom chatRoom) {
        return new ChatRoomOut(
                chatRoom.getId().toString(),
                chatRoom.getName(),
                chatRoom.getOwner().getId(),
                chatRoom.getOwner().getName()
        );
    }
}
