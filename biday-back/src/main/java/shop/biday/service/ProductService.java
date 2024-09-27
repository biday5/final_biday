package shop.biday.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Map.Entry<Long, ProductModel>> findByProductId(Long id);
    Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);
    ProductEntity save(String token, ProductModel product);
    ProductEntity update(String token, ProductModel product);
    String deleteById(String token, Long id);
}
