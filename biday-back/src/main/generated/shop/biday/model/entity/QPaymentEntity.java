package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentEntity is a Querydsl query type for PaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEntity extends EntityPathBase<PaymentEntity> {

    private static final long serialVersionUID = 813852628L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentEntity paymentEntity = new QPaymentEntity("paymentEntity");

    public final DateTimePath<java.time.LocalDateTime> approvedAt = createDateTime("approvedAt", java.time.LocalDateTime.class);

    public final QAwardEntity award;

    public final NumberPath<Long> balanceAmount = createNumber("balanceAmount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath currency = createString("currency");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath orderId = createString("orderId");

    public final StringPath paymentKey = createString("paymentKey");

    public final EnumPath<shop.biday.model.entity.enums.PaymentMethod> paymentMethod = createEnum("paymentMethod", shop.biday.model.entity.enums.PaymentMethod.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", shop.biday.model.entity.enums.PaymentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> requestedAt = createDateTime("requestedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> suppliedAmount = createNumber("suppliedAmount", Long.class);

    public final NumberPath<Long> totalAmount = createNumber("totalAmount", Long.class);

    public final StringPath type = createString("type");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUserEntity user;

    public final NumberPath<Long> vat = createNumber("vat", Long.class);

    public QPaymentEntity(String variable) {
        this(PaymentEntity.class, forVariable(variable), INITS);
    }

    public QPaymentEntity(Path<? extends PaymentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentEntity(PathMetadata metadata, PathInits inits) {
        this(PaymentEntity.class, metadata, inits);
    }

    public QPaymentEntity(Class<? extends PaymentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.award = inits.isInitialized("award") ? new QAwardEntity(forProperty("award"), inits.get("award")) : null;
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

