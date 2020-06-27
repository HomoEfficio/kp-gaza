package io.homo_efficio.kpgaza.money_distribution.service;

import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomOut;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatUserOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ChatRoomService {

    ChatRoomOut create(ChatRoomIn chatRoomIn);
    ChatUserOut createChatUser(UUID chatRoomId, Long chatterId);
    Page<ChatRoomOut> listChatRooms(Pageable pageable);
}
