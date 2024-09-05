package shop.biday.model.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ImageModel {
    private Long id;
    private String url;
    private Long referenceId;
    private String type; // 브랜드: 01, 상품: 02, 경매: 03, 환불: 04
    private LocalDateTime createdAt;
    private int ratingValue;
}
