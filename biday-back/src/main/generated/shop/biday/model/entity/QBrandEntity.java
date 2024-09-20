package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBrandEntity is a Querydsl query type for BrandEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrandEntity extends EntityPathBase<BrandEntity> {

    private static final long serialVersionUID = 391099221L;

    public static final QBrandEntity brandEntity = new QBrandEntity("brandEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QBrandEntity(String variable) {
        super(BrandEntity.class, forVariable(variable));
    }

    public QBrandEntity(Path<? extends BrandEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBrandEntity(PathMetadata metadata) {
        super(BrandEntity.class, metadata);
    }

}

