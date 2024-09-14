package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.WishEntity;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long>, QWishRepository {

    List<?> findByUserId(Long id);

}
