package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.AddressEntity;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    long countByUserId(Long id);

    List<AddressEntity> findByUserIdAndPick(Long userId, Boolean pick);

    AddressEntity findByUserId(Long id);


    List<AddressEntity> findAllByUserId(Long id);
}
