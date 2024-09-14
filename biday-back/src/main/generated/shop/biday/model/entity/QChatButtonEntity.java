package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatButtonEntity is a Querydsl query type for ChatButtonEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatButtonEntity extends EntityPathBase<ChatButtonEntity> {

    private static final long serialVersionUID = -1693302014L;

    public static final QChatButtonEntity chatButtonEntity = new QChatButtonEntity("chatButtonEntity");

    public final StringPath buttonDescription = createString("buttonDescription");

    public final StringPath buttonImg = createString("buttonImg");

    public final StringPath buttonLink = createString("buttonLink");

    public final StringPath buttonName = createString("buttonName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QChatButtonEntity(String variable) {
        super(ChatButtonEntity.class, forVariable(variable));
    }

    public QChatButtonEntity(Path<? extends ChatButtonEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatButtonEntity(PathMetadata metadata) {
        super(ChatButtonEntity.class, metadata);
    }

}

