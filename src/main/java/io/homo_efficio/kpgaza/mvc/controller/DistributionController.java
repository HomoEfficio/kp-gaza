package io.homo_efficio.kpgaza.mvc.controller;

import io.homo_efficio.kpgaza.mvc.dto.DistributionIn;
import io.homo_efficio.kpgaza.mvc.dto.DistributionOut;
import io.homo_efficio.kpgaza.mvc.dto.DistributionToken;
import io.homo_efficio.kpgaza.mvc.service.DistributionService;
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
}
