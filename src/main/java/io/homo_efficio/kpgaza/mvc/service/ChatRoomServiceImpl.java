package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.mvc.domain.model.KUser;
import io.homo_efficio.kpgaza.mvc.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.KUserRepository;
import io.homo_efficio.kpgaza.mvc.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.mvc.dto.ChatRoomOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final KUserRepository kUserRepository;

    @Override
    public ChatRoomOut create(ChatRoomIn chatRoomIn) {
        Long ownerId = chatRoomIn.getOwnerId();
        KUser owner = kUserRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException(String.format("방장 [%d]이 존재하지 않습니다.", ownerId)));
        ChatRoom chatRoom = owner.createChatRoom(chatRoomIn.getName());
        return ChatRoomOut.from(chatRoomRepository.save(chatRoom));
    }
}
