package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuctionEntity is a Querydsl query type for AuctionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuctionEntity extends EntityPathBase<AuctionEntity> {

    private static final long serialVersionUID = 782119505L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuctionEntity auctionEntity = new QAuctionEntity("auctionEntity");

    public final QAwardEntity award;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> currentBid = createNumber("currentBid", Long.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProductEntity product;

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> startingBid = createNumber("startingBid", Long.class);

    public final BooleanPath status = createBoolean("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAuctionEntity(String variable) {
        this(AuctionEntity.class, forVariable(variable), INITS);
    }

    public QAuctionEntity(Path<? extends AuctionEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuctionEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuctionEntity(PathMetadata metadata, PathInits inits) {
        this(AuctionEntity.class, metadata, inits);
    }

    public QAuctionEntity(Class<? extends AuctionEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.award = inits.isInitialized("award") ? new QAwardEntity(forProperty("award"), inits.get("award")) : null;
        this.product = inits.isInitialized("product") ? new QProductEntity(forProperty("product"), inits.get("product")) : null;
    }

}

