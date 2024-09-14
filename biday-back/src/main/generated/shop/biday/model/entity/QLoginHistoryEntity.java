package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoginHistoryEntity is a Querydsl query type for LoginHistoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginHistoryEntity extends EntityPathBase<LoginHistoryEntity> {

    private static final long serialVersionUID = 941653315L;

    public static final QLoginHistoryEntity loginHistoryEntity = new QLoginHistoryEntity("loginHistoryEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLoginHistoryEntity(String variable) {
        super(LoginHistoryEntity.class, forVariable(variable));
    }

    public QLoginHistoryEntity(Path<? extends LoginHistoryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoginHistoryEntity(PathMetadata metadata) {
        super(LoginHistoryEntity.class, metadata);
    }

}

