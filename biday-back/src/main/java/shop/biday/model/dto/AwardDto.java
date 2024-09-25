package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.domain.UserModel;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardDto {
    private Long id;
    private Long auction;
    private String user;
    private LocalDateTime bidedAt;
    private BigInteger currentBid;
    private int count;
}
