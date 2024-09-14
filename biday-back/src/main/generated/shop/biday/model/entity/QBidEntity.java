package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBidEntity is a Querydsl query type for BidEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBidEntity extends EntityPathBase<BidEntity> {

    private static final long serialVersionUID = -469353717L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBidEntity bidEntity = new QBidEntity("bidEntity");

    public final QAuctionEntity auction;

    public final BooleanPath award = createBoolean("award");

    public final DateTimePath<java.time.LocalDateTime> bidedAt = createDateTime("bidedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> currentBid = createNumber("currentBid", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBidEntity(String variable) {
        this(BidEntity.class, forVariable(variable), INITS);
    }

    public QBidEntity(Path<? extends BidEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBidEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBidEntity(PathMetadata metadata, PathInits inits) {
        this(BidEntity.class, metadata, inits);
    }

    public QBidEntity(Class<? extends BidEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new QAuctionEntity(forProperty("auction"), inits.get("auction")) : null;
    }

}

