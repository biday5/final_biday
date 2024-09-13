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

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> bidId = createNumber("bidId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> finalBid = createNumber("finalBid", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> paymentDate = createDateTime("paymentDate", java.time.LocalDateTime.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentMethod> paymentMethod = createEnum("paymentMethod", shop.biday.model.entity.enums.PaymentMethod.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentStatus> paymentStatus = createEnum("paymentStatus", shop.biday.model.entity.enums.PaymentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

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

