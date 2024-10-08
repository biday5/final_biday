package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel {
    private Long id;
    private Long userId;
    private Long typeId;
    private String message;
    private LocalDateTime createdAt;
    private boolean status;
}
