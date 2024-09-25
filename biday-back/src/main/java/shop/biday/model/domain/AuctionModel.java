package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AwardDto;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.dto.SizeDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.model.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AuctionModel {
    private Long id;
    private String user;
    private SizeDto size;
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
