package shop.biday.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "images")
@AllArgsConstructor
@NoArgsConstructor
public class ImageDocument {
    @Id
    private String id;
    private String name;
    private String ext;
    private String url;
    private String type;
    private Long referencedId;
    @CreatedDate
    private LocalDateTime createdAt;
}
