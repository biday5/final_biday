package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRefundEntity is a Querydsl query type for RefundEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefundEntity extends EntityPathBase<RefundEntity> {

    private static final long serialVersionUID = -1042760240L;

    public static final QRefundEntity refundEntity = new QRefundEntity("refundEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> refundedAt = createDateTime("refundedAt", java.time.LocalDateTime.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentStatus> status = createEnum("status", shop.biday.model.entity.enums.PaymentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QRefundEntity(String variable) {
        super(RefundEntity.class, forVariable(variable));
    }

    public QRefundEntity(Path<? extends RefundEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRefundEntity(PathMetadata metadata) {
        super(RefundEntity.class, metadata);
    }

}

