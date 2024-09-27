package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ChatButtonModel {
    private Long id;
    private String buttonName;
    private String buttonLink;
    private String buttonImg;
    private String buttonDescription;
}
