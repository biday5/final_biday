package shop.biday.model.domain;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.BidDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.BidEntity;
import shop.biday.model.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuctionModel {
    private Long id;
    private Long userId;
    private ProductDto product;
    private String description;
    private Long startingBid;
    private Long currentBid;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private List<ImageModel> images;
    private BidDto bid;
}
