package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRatingEntity is a Querydsl query type for RatingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRatingEntity extends EntityPathBase<RatingEntity> {

    private static final long serialVersionUID = 1976016949L;

    public static final QRatingEntity ratingEntity = new QRatingEntity("ratingEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QRatingEntity(String variable) {
        super(RatingEntity.class, forVariable(variable));
    }

    public QRatingEntity(Path<? extends RatingEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRatingEntity(PathMetadata metadata) {
        super(RatingEntity.class, metadata);
    }

}

