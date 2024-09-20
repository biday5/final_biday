package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnnouncementEntity is a Querydsl query type for AnnouncementEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnnouncementEntity extends EntityPathBase<AnnouncementEntity> {

    private static final long serialVersionUID = -596049761L;

    public static final QAnnouncementEntity announcementEntity = new QAnnouncementEntity("announcementEntity");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAnnouncementEntity(String variable) {
        super(AnnouncementEntity.class, forVariable(variable));
    }

    public QAnnouncementEntity(Path<? extends AnnouncementEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnnouncementEntity(PathMetadata metadata) {
        super(AnnouncementEntity.class, metadata);
    }

}

