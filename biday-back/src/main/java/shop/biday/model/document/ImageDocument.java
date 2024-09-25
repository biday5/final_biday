package shop.biday.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "images")
public class ImageDocument {
    @Id
    private String id;
    @Field("originalName")
    private String originalName;
    @Field("uploadName")
    private String uploadName;
    //    private String ext;
    @Field("uploadPath")
    private String uploadPath;
    @Field("uploadUrl")
    private String uploadUrl;
    @Field("type")
    private String type;
    @Field("referencedId")
    private Long referencedId;
//    private String name;
//    private String ext;
//    private String url;
//    private String type;
//    private Long referencedId;
    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;
}
