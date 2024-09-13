package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;

public interface QProductRepository {
    ProductModel findById(Long id);
    Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);
}
