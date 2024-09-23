package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.dto.ProductDto;

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
    private List<ImageModel> images;
    private AwardDto award;
}
