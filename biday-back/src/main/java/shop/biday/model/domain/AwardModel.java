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
public class AwardModel {
    private Long id;
    private AuctionDto auction;
    private UserModel user;
    private LocalDateTime bidedAt;
    private Long currentBid;
    private int count;
}
