package shop.biday.model.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.ImageModel;

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
    private Long wishes;
    private Long auctions; // size 리스트르 ㄹ가져와서 거기에 들어잇는 product id로 조회해서 카운트?
}
