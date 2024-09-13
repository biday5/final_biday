package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSellerPaymentEntity is a Querydsl query type for SellerPaymentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSellerPaymentEntity extends EntityPathBase<SellerPaymentEntity> {

    private static final long serialVersionUID = 2014022229L;

    public static final QSellerPaymentEntity sellerPaymentEntity = new QSellerPaymentEntity("sellerPaymentEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> fee = createNumber("fee", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> netAmount = createNumber("netAmount", Long.class);

    public final DateTimePath<java.time.LocalDateTime> paymentDate = createDateTime("paymentDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentMethod> paymentMethod = createEnum("paymentMethod", shop.biday.model.entity.enums.PaymentMethod.class);

    public final EnumPath<shop.biday.model.entity.enums.PaymentStatus> status = createEnum("status", shop.biday.model.entity.enums.PaymentStatus.class);

    public final NumberPath<Long> totalAmount = createNumber("totalAmount", Long.class);

    public final StringPath transactionId = createString("transactionId");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QSellerPaymentEntity(String variable) {
        super(SellerPaymentEntity.class, forVariable(variable));
    }

    public QSellerPaymentEntity(Path<? extends SellerPaymentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSellerPaymentEntity(PathMetadata metadata) {
        super(SellerPaymentEntity.class, metadata);
    }

}

