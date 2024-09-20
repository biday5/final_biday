package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.model.domain.AnnouncementModel;
import shop.biday.model.entity.AnnouncementEntity;
import shop.biday.model.repository.AnnouncementRepository;
import shop.biday.service.AnnouncementService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    private AnnouncementModel toModel(AnnouncementEntity entity) {
        return AnnouncementModel.builder()
                .id(entity.getId()) // 공지사항 아이디
                .userId(entity.getUserId()) // 작성자 아이디
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private AnnouncementEntity toEntity(AnnouncementModel model) {
        return AnnouncementEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .title(model.getTitle())
                .content(model.getContent())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public List<AnnouncementModel> findAll() {
        return announcementRepository.findAll().stream()  // 엔티티 리스트를 스트림으로 변환
                .map(this::toModel)  // 각 엔티티를 모델로 변환
                .collect(Collectors.toList());  // 결과를 리스트로 수집하여 반환
    }

    @Override
    public AnnouncementModel save(AnnouncementModel announcementModel) {
        AnnouncementEntity savedEntity = announcementRepository.save(toEntity(announcementModel));
        return toModel(savedEntity);
    }

    @Override
    public Optional<AnnouncementModel> findById(Long id) {
        return announcementRepository.findById(id).map(this::toModel);
    }

    @Override
    public void deleteById(Long id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return announcementRepository.existsById(id);
    }
}
