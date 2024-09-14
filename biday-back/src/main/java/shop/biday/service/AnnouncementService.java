package shop.biday.service;

import shop.biday.model.domain.AnnouncementModel;

import java.util.List;
import java.util.Optional;

public interface AnnouncementService {
    List<AnnouncementModel> findAll();
    AnnouncementModel save(AnnouncementModel announcementModel);
    Optional<AnnouncementModel> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
