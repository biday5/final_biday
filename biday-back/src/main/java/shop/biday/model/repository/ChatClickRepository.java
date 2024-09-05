package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.ChatClickEntity;

@Repository
public interface ChatClickRepository extends JpaRepository<ChatClickEntity, Long> {
}
