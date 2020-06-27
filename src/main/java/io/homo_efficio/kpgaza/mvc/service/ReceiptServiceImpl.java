package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc._common.exception.InvalidReceiptException;
import io.homo_efficio.kpgaza.mvc.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.mvc.domain.model.Distribution;
import io.homo_efficio.kpgaza.mvc.domain.model.KUser;
import io.homo_efficio.kpgaza.mvc.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.DistributionRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.KUserRepository;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptIn;
import io.homo_efficio.kpgaza.mvc.dto.ReceiptOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-27
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final DistributionRepository distributionRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final KUserRepository kUserRepository;

    @Override
    public ReceiptOut create(ReceiptIn receiptIn) {
        String token = receiptIn.getToken();
        Distribution distribution = distributionRepository.findByToken(token)
                .orElseThrow(() -> new InvalidReceiptException(
                        String.format("토큰 [%s]에 대한 뿌리기가 존재하지 않아 수령할 수 없습니다.", token)));

        UUID chatRoomId = receiptIn.getChatRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new InvalidReceiptException(
                        String.format("대화방 [%s]이 존재하지 않아 수령할 수 없습니다.", chatRoomId)));

        Long receiverId = receiptIn.getReceiverId();
        KUser receiver = kUserRepository.findById(receiverId)
                .orElseThrow(() -> new InvalidReceiptException(
                        String.format("사용자 [%s]이 존재하지 않아 수령할 수 없습니다.", receiverId)));

        return ReceiptOut.from(receiver.receiveMoney(distribution));
    }
}
