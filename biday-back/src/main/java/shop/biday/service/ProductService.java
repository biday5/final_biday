package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;

public interface ProductService {
    ProductModel findById(long id);
    ProductEntity save(ProductModel product);
    ProductEntity update(ProductModel product);
    void deleteById(Long id);
    Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);
}
