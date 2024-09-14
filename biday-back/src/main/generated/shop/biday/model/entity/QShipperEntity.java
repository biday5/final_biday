package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShipperEntity is a Querydsl query type for ShipperEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShipperEntity extends EntityPathBase<ShipperEntity> {

    private static final long serialVersionUID = 246538991L;

    public static final QShipperEntity shipperEntity = new QShipperEntity("shipperEntity");

    public final StringPath carrier = createString("carrier");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath deliveryAddress = createString("deliveryAddress");

    public final DateTimePath<java.time.LocalDateTime> deliveryDate = createDateTime("deliveryDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> estimatedDeliveryDate = createDateTime("estimatedDeliveryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> shipmentDate = createDateTime("shipmentDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath trackingNumber = createString("trackingNumber");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QShipperEntity(String variable) {
        super(ShipperEntity.class, forVariable(variable));
    }

    public QShipperEntity(Path<? extends ShipperEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShipperEntity(PathMetadata metadata) {
        super(ShipperEntity.class, metadata);
    }

}

