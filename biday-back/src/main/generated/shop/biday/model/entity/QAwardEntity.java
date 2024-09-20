package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAwardEntity is a Querydsl query type for AwardEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAwardEntity extends EntityPathBase<AwardEntity> {

    private static final long serialVersionUID = -413210197L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAwardEntity awardEntity = new QAwardEntity("awardEntity");

    public final QAuctionEntity auction;

    public final DateTimePath<java.time.LocalDateTime> bidedAt = createDateTime("bidedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> currentBid = createNumber("currentBid", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUserEntity user;

    public QAwardEntity(String variable) {
        this(AwardEntity.class, forVariable(variable), INITS);
    }

    public QAwardEntity(Path<? extends AwardEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAwardEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAwardEntity(PathMetadata metadata, PathInits inits) {
        this(AwardEntity.class, metadata, inits);
    }

    public QAwardEntity(Class<? extends AwardEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new QAuctionEntity(forProperty("auction"), inits.get("auction")) : null;
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

