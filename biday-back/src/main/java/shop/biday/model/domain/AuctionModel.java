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
public class AuctionModel {
    private Long id;
    private Long userId;
    private Long productId;
    private String description;
    private Long startingBid;
    private Long currentBid;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
