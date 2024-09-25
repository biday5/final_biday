package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Long wishes;
    private ImageModel image;
    //        private List<ImageModel> image;
    private Set<SizeModel> sizes = new HashSet<>();
//    private Set<AuctionDto> auctions = new HashSet<>();
//    private List<SizeModel> sizes;
}
