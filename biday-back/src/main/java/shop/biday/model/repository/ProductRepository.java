package shop.biday.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, QProductRepository {
    boolean existsById(Long id);
    ProductEntity save(ProductModel product);
    void deleteById(Long id);
}
