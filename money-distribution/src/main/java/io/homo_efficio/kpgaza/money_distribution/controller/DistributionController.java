package io.homo_efficio.kpgaza.money_distribution.controller;

import io.homo_efficio.kpgaza.money_distribution.dto.DistributionIn;
import io.homo_efficio.kpgaza.money_distribution.dto.DistributionOut;
import io.homo_efficio.kpgaza.money_distribution.dto.DistributionToken;
import io.homo_efficio.kpgaza.money_distribution.service.DistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@RequestMapping("/distributions")
@RestController
@RequiredArgsConstructor
public class DistributionController {

    private final DistributionService distributionService;

    @PostMapping
    public ResponseEntity<DistributionToken> createDistribution(@RequestHeader("X-USER-ID") Long distributorId,
                                                                @RequestHeader("X-ROOM-ID") UUID chatRoomId,
                                                                @RequestBody DistributionIn distributionIn) {
        DistributionIn newDistribution = new DistributionIn(distributorId, chatRoomId, distributionIn.getAmount(), distributionIn.getTargets());
        DistributionOut distributionOut = distributionService.createDistribution(newDistribution);
        return ResponseEntity.ok(new DistributionToken(distributionOut.getToken()));
    }

    @GetMapping
    public ResponseEntity<DistributionOut> findByToken(@RequestHeader("X-USER-ID") Long requesterId,
                                                       @RequestParam String token) {
        return ResponseEntity.ok(distributionService.findByToken(token, requesterId));
    }
}
