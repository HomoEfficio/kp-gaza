package io.homo_efficio.kpgaza.money_distribution.controller;

import io.homo_efficio.kpgaza.money_distribution.dto.ReceiptAmount;
import io.homo_efficio.kpgaza.money_distribution.dto.ReceiptIn;
import io.homo_efficio.kpgaza.money_distribution.dto.ReceiptOut;
import io.homo_efficio.kpgaza.money_distribution.service.ReceiptService;
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
