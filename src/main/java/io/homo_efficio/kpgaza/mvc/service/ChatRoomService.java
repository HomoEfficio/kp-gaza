package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.mvc.dto.ChatRoomOut;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ChatRoomService {

    ChatRoomOut create(ChatRoomIn chatRoomIn);
}
