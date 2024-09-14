package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRefundEntity is a Querydsl query type for RefundEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefundEntity extends EntityPathBase<RefundEntity> {

    private static final long serialVersionUID = -1042760240L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRefundEntity refundEntity = new QRefundEntity("refundEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> canceledAt = createDateTime("canceledAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPaymentEntity payment;

    public final StringPath reason = createString("reason");

    public final StringPath status = createString("status");

    public final StringPath transactionKey = createString("transactionKey");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QRefundEntity(String variable) {
        this(RefundEntity.class, forVariable(variable), INITS);
    }

    public QRefundEntity(Path<? extends RefundEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRefundEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRefundEntity(PathMetadata metadata, PathInits inits) {
        this(RefundEntity.class, metadata, inits);
    }

    public QRefundEntity(Class<? extends RefundEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new QPaymentEntity(forProperty("payment")) : null;
    }

}

