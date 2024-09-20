package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Long id;
    private String brand;
    private String category;
    private String name;
    private String subName;
    private String productCode;
    private Long price;
    private String color;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageModel image;
    @Setter
    private List<AuctionDto> auctions;
}
