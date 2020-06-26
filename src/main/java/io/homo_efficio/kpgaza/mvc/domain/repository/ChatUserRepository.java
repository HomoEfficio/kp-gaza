package io.homo_efficio.kpgaza.mvc.domain.repository;

import io.homo_efficio.kpgaza.mvc.domain.model.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
}
