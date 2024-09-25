package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.SizeModel;
import shop.biday.model.entity.SizeEntity;
import shop.biday.model.entity.enums.Size;
import shop.biday.model.repository.ProductRepository;
import shop.biday.model.repository.SizeRepository;
import shop.biday.service.SizeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    @Override
    public List<SizeEntity> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Optional<SizeEntity> findById(Long id) {
        return sizeRepository.findById(id);
    }

    @Override
    public List<SizeEntity> findAllByProductId(Long productId) {
        return sizeRepository.findAllByProductId(productId);
    }

    @Override
    public SizeEntity save(String token, SizeModel size) {
        return sizeRepository.save(SizeEntity.builder()
                .size(Size.valueOf(size.getSize()))
                .product(productRepository.findByName(size.getSizeProduct()))
                .build());
    }

    @Override
    public SizeEntity update(String token, SizeModel size) {
        return sizeRepository.save(SizeEntity.builder()
                .size(Size.valueOf(size.getSize()))
                .product(productRepository.findByName(size.getSizeProduct()))
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Override
    public void deleteById(String token, Long id) {
        sizeRepository.deleteById(id);
    }
}
