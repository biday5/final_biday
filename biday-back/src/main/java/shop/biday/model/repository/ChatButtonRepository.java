package shop.biday.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.biday.model.entity.ChatButtonEntity;

@Repository
public interface ChatButtonRepository extends JpaRepository<ChatButtonEntity, Long> {
}
