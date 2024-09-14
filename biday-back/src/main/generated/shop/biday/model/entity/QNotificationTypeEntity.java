package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotificationTypeEntity is a Querydsl query type for NotificationTypeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationTypeEntity extends EntityPathBase<NotificationTypeEntity> {

    private static final long serialVersionUID = 1536747805L;

    public static final QNotificationTypeEntity notificationTypeEntity = new QNotificationTypeEntity("notificationTypeEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QNotificationTypeEntity(String variable) {
        super(NotificationTypeEntity.class, forVariable(variable));
    }

    public QNotificationTypeEntity(Path<? extends NotificationTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotificationTypeEntity(PathMetadata metadata) {
        super(NotificationTypeEntity.class, metadata);
    }

}

