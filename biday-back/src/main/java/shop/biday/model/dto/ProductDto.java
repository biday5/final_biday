package shop.biday.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.ImageModel;

import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String brand;
    private String category;
    private String name;
    private String subName;
    private String productCode;
    private Long price;
    private String color;
    private ImageModel image;
    private Long auctions;
    private Long wishes;
}
