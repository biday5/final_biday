package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.model.repository.BrandRepository;
import shop.biday.service.BrandService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<BrandEntity> findAll() {
        try {
            return brandRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<BrandEntity> findById(Long id) {
        try {
            return brandRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public BrandEntity save(BrandModel brand) {
        try {
            return brandRepository.save(brand);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public BrandEntity update(BrandModel brand) {
        try {
            return brandRepository.save(brand);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            brandRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
