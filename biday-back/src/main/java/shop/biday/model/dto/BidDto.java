package shop.biday.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BidDto {
    private Long id;
    private Long auction;
    private Long userId;
    private LocalDateTime bidedAt;
    private int currentBid;
    private boolean award;
}
