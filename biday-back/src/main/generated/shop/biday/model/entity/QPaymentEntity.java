package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentEntity is a Querydsl query type for PaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentEntity extends EntityPathBase<PaymentEntity> {

    private static final long serialVersionUID = 813852628L;

    public static final QPaymentEntity paymentEntity = new QPaymentEntity("paymentEntity");

    public final DateTimePath<java.time.LocalDateTime> approvedAt = createDateTime("approvedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> balanceAmount = createNumber("balanceAmount", Long.class);

    public final NumberPath<Long> bidId = createNumber("bidId", Long.class);

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

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Long> vat = createNumber("vat", Long.class);

    public QPaymentEntity(String variable) {
        super(PaymentEntity.class, forVariable(variable));
    }

    public QPaymentEntity(Path<? extends PaymentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentEntity(PathMetadata metadata) {
        super(PaymentEntity.class, metadata);
    }

}

