package shop.biday.service;

import shop.biday.model.domain.FaqModel;

import java.util.List;
import java.util.Optional;

public interface FaqService {
    List<FaqModel> findAll();

    FaqModel save(FaqModel questionModel);

    Optional<FaqModel> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}
