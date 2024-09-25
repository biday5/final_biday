package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import shop.biday.model.ProductTest;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface QProductRepository {
//    List<ProductTest> findByProductTest(Long id);
    Map<Long, ProductModel> findByProductId(Long id, String name);
    Slice<ProductDto> findProducts(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId);
}
