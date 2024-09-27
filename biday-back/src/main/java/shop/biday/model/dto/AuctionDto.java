package shop.biday.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.ImageModel;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDto {
    private Long id;
    private String userId;
    private String product;
    private String size;
    private Long startingBid;
    private Long currentBid;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}