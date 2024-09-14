package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.FaqModel;
import shop.biday.model.entity.FaqEntity;
import shop.biday.model.repository.FaqRepository;
import shop.biday.service.FaqService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;

    // 엔티티를 모델로 변환하는 메서드
    private FaqModel toModel(FaqEntity entity) {
        return FaqModel.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

    // 모델을 엔티티로 변환하는 메서드
    private FaqEntity toEntity(FaqModel model) {
        return FaqEntity.builder()
                .id(model.getId())
                .title(model.getTitle())
                .content(model.getContent())
                .build();
    }

    @Override
    public List<FaqModel> findAll() {
        return faqRepository.findAll().stream()
                .map(this::toModel) // 엔티티를 모델로 변환
                .collect(Collectors.toList());
    }

    @Override
    public FaqModel save(FaqModel faqModel) {
        FaqEntity savedEntity = faqRepository.save(toEntity(faqModel)); // 모델을 엔티티로 변환하여 저장
        return toModel(savedEntity); // 저장된 엔티티를 다시 모델로 변환하여 반환
    }

    @Override
    public Optional<FaqModel> findById(Long id) {
        return faqRepository.findById(id)
                .map(this::toModel); // 엔티티를 모델로 변환하여 반환
    }

    @Override
    public void deleteById(Long id) {
        faqRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return faqRepository.existsById(id);
    }
}
