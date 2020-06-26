package io.homo_efficio.kpgaza.mvc.controller;

import io.homo_efficio.kpgaza.mvc.dto.ReceiptAmount;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptIn;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptOut;
import io.homo_efficio.kpgaza.mvc.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
@RequestMapping("/receipts")
@RestController
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<ReceiptAmount> create(@RequestHeader("X-USER-ID") Long receiverId,
                                                @RequestHeader("X-ROOM-ID") UUID chatRoomId,
                                                @RequestBody ReceiptIn receiptIn) {
        ReceiptIn newReceiptIn = new ReceiptIn(receiptIn.getToken(), receiverId, chatRoomId);
        ReceiptOut receiptOut = receiptService.create(newReceiptIn);
        return ResponseEntity.ok(new ReceiptAmount(receiptOut.getAmount()));
    }
}
