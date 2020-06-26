package io.homo_efficio.kpgaza.mvc.controller;

import io.homo_efficio.kpgaza.mvc.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.mvc.dto.ChatRoomOut;
import io.homo_efficio.kpgaza.mvc.dto.ChatUserOut;
import io.homo_efficio.kpgaza.mvc.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@RequestMapping("/chat-rooms")
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoomOut> create(@RequestBody ChatRoomIn chatRoomIn) {
        return ResponseEntity.ok(chatRoomService.create(chatRoomIn));
    }

    @PostMapping("/{chatRoomId}/chatters/{chatterId}")
    public ResponseEntity<ChatUserOut> createChatUser(@PathVariable UUID chatRoomId, @PathVariable Long chatterId) {
        return ResponseEntity.ok(chatRoomService.createChatUser(chatRoomId, chatterId));
    }
}
