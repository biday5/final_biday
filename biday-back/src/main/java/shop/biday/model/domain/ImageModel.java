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
    private String id;
    private String name;
    private String ext;
    private String url;
    private String type;
    private Long referenceId;
    private LocalDateTime createdAt;
//    private int ratingValue;
}
