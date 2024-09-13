package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMessengerEntity is a Querydsl query type for MessengerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessengerEntity extends EntityPathBase<MessengerEntity> {

    private static final long serialVersionUID = 584830049L;

    public static final QMessengerEntity messengerEntity = new QMessengerEntity("messengerEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QMessengerEntity(String variable) {
        super(MessengerEntity.class, forVariable(variable));
    }

    public QMessengerEntity(Path<? extends MessengerEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMessengerEntity(PathMetadata metadata) {
        super(MessengerEntity.class, metadata);
    }

}

