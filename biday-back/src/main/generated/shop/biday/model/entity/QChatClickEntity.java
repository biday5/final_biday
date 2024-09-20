package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatClickEntity is a Querydsl query type for ChatClickEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatClickEntity extends EntityPathBase<ChatClickEntity> {

    private static final long serialVersionUID = -1075414882L;

    public static final QChatClickEntity chatClickEntity = new QChatClickEntity("chatClickEntity");

    public final NumberPath<Long> buttonId = createNumber("buttonId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> clickTime = createDateTime("clickTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QChatClickEntity(String variable) {
        super(ChatClickEntity.class, forVariable(variable));
    }

    public QChatClickEntity(Path<? extends ChatClickEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatClickEntity(PathMetadata metadata) {
        super(ChatClickEntity.class, metadata);
    }

}

