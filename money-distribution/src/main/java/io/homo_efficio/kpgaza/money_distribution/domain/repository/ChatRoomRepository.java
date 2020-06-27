package io.homo_efficio.kpgaza.money_distribution.domain.repository;

import io.homo_efficio.kpgaza.money_distribution.domain.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
}
