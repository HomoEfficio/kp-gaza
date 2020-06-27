package io.homo_efficio.kpgaza.mvc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.homo_efficio.kpgaza.mvc.domain.model.Distribution;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-06-26
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DistributionOut {

    private Long id;
    private String token;
    private Long distributorId;
    private UUID chatRoomId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime distributedAt;
    private Integer amount;
    private Integer targets;
    private Integer receivedAmount;
    private List<ReceiptOut> receipts;


    public static DistributionOut from(Distribution distribution) {
        return new DistributionOut(
                distribution.getId(),
                distribution.getToken(),
                distribution.getDistributor().getId(),
                distribution.getChatRoom().getId(),
                distribution.getCreatedDateTime(),
                distribution.getAmount(),
                distribution.getTargets(),
                distribution.getReceivedAmount(),
                distribution.getClosedReceipts().stream()
                        .map(ReceiptOut::from).collect(Collectors.toList())
        );
    }
}
