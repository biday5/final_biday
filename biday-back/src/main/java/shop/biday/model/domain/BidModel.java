package shop.biday.model.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class BidModel {
    private Long id;
    private Long auctionId;
    private Long userId;
    private LocalDateTime bidedAt;
    private int currentBid;
    private int count;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean award;
}
