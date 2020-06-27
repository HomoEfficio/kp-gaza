package io.homo_efficio.kpgaza.money_distribution.service;

import io.homo_efficio.kpgaza.money_distribution._common.exception.EntityNotFoundException;
import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.money_distribution.domain.model.KUser;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.ChatUserRepository;
import io.homo_efficio.kpgaza.money_distribution.domain.repository.KUserRepository;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomOut;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatUserOut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    private final ChatUserRepository chatUserRepository;

    @Override
    public ChatRoomOut create(ChatRoomIn chatRoomIn) {
        Long ownerId = chatRoomIn.getOwnerId();
        KUser owner = kUserRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException(KUser.class, String.format("방장 [%d]이 존재하지 않습니다.", ownerId)));
        ChatRoom chatRoom = owner.createChatRoom(chatRoomIn.getName());
        return ChatRoomOut.from(chatRoomRepository.save(chatRoom));
    }

    @Override
    public ChatUserOut createChatUser(UUID chatRoomId, Long chatterId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ChatRoom.class, String.format("대화방 [%s]이 존재하지 않습니다.", chatRoomId.toString())));
        KUser chatter = kUserRepository.findById(chatterId)
                .orElseThrow(() -> new EntityNotFoundException(KUser.class, String.format("사용자 [%s]가 존재하지 않습니다.", chatterId.toString())));
        return ChatUserOut.from(chatUserRepository.save(chatter.enterChatRoom(chatRoom)));
    }

    @Override
    public Page<ChatRoomOut> listChatRooms(Pageable pageable) {
        return chatRoomRepository.findAll(pageable)
                .map(ChatRoomOut::from);
    }
}
