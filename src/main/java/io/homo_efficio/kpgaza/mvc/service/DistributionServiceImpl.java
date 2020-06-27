package io.homo_efficio.kpgaza.mvc.service;

import io.homo_efficio.kpgaza.mvc.domain.model.ChatRoom;
import io.homo_efficio.kpgaza.mvc.domain.model.Distribution;
import io.homo_efficio.kpgaza.mvc.domain.model.KUser;
import io.homo_efficio.kpgaza.mvc.domain.repository.ChatRoomRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.ChatUserRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.DistributionRepository;
import io.homo_efficio.kpgaza.mvc.domain.repository.KUserRepository;
import io.homo_efficio.kpgaza.mvc.dto.DistributionIn;
import io.homo_efficio.kpgaza.mvc.dto.DistributionOut;
import lombok.RequiredArgsConstructor;
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
public class DistributionServiceImpl implements DistributionService {

    private final ChatUserRepository chatUserRepository;
    private final KUserRepository kUserRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final DistributionRepository distributionRepository;

    @Override
    public DistributionOut createDistribution(DistributionIn distributionIn) {
        Long distributorId = distributionIn.getDistributorId();
        KUser distributor = kUserRepository.findById(distributorId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("사용자 [%d]가 존재하지 않아 뿌리기를 할 수 없습니다.", distributorId)));

        UUID chatRoomId = distributionIn.getChatRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("대화방 [%s]이 존재하지 않아 뿌리기를 할 수 않습니다.", chatRoomId)));

        Distribution distribution = distributionRepository.save(
                distributor.distributeMoney(chatRoom, distributionIn.getAmount(), distributionIn.getTargets()));

        return DistributionOut.from(distribution);
    }

    @Override
    public DistributionOut findByToken(String token, Long requesterId) {
        Distribution distribution = distributionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException(
                        String.format("토큰 [%s]에 해당하는 뿌리기가 존재하지 않습니다.", token)));
        KUser requester = kUserRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("사용자 [%d]가 존재하지 않아 뿌리기 정보를 조회할 수 없습니다.", requesterId)));

        return DistributionOut.from(requester.showDistribution(distribution));
    }
}
