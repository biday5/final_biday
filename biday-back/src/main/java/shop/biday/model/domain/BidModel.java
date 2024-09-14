package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AuctionDto;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BidModel {
    private Long id;
    private AuctionDto auction;
    private Long userId;
    private LocalDateTime bidedAt;
    private int currentBid;
    private int count;
    private LocalDateTime createdAt;
    private boolean award;
    private PaymentTempModel paymentTemp;
}
