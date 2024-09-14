package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.QProductRepository;
import shop.biday.service.ProductService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final QProductRepository productRepository;

    @Override
    public ProductEntity save(ProductModel product) {
        try {
            return repository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ProductEntity update(ProductModel product) {
        try {
            if (repository.existsById(product.getId())) {
                return repository.save(product);
            } else return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public ProductModel findById(long id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Slice<ProductDto> findByFilter(Pageable pageable, Long categoryId, Long brandId, String keyword, String color, String order, Long lastItemId) {
        try {
            return productRepository.findByFilter(pageable, categoryId, brandId, keyword, color, order, lastItemId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
