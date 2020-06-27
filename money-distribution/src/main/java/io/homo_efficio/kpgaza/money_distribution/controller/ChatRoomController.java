package io.homo_efficio.kpgaza.money_distribution.controller;

import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomIn;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatRoomOut;
import io.homo_efficio.kpgaza.money_distribution.dto.ChatUserOut;
import io.homo_efficio.kpgaza.money_distribution.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<Page<ChatRoomOut>> list(@PageableDefault(size = 4) Pageable pageable) {
        return ResponseEntity.ok(chatRoomService.listChatRooms(pageable));
    }
}
