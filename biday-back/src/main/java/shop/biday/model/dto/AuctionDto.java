package shop.biday.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.BidModel;
import shop.biday.model.domain.ProductModel;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDto {
    private Long id;
    private Long userId;
    private String product;
    private Long startingBid;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //    private List<ImageModel> images;
}
