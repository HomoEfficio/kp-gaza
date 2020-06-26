package io.homo_efficio.kpgaza.mvc.domain.repository;

import io.homo_efficio.kpgaza.mvc.domain.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    Optional<ChatUser> findByChatRoom_IdAndChatter_Id(UUID chatRoomId, Long chatterId);
}
