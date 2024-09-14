package shop.biday.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccountEntity is a Querydsl query type for AccountEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccountEntity extends EntityPathBase<AccountEntity> {

    private static final long serialVersionUID = -1486174533L;

    public static final QAccountEntity accountEntity = new QAccountEntity("accountEntity");

    public final NumberPath<Integer> accountNumber = createNumber("accountNumber", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createAt = createDateTime("createAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAccountEntity(String variable) {
        super(AccountEntity.class, forVariable(variable));
    }

    public QAccountEntity(Path<? extends AccountEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccountEntity(PathMetadata metadata) {
        super(AccountEntity.class, metadata);
    }

}

