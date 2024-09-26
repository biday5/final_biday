package shop.biday.model.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.repository.PaymentRepository;

import java.util.List;

import static com.querydsl.core.group.GroupBy.*;

@Repository
@RequiredArgsConstructor
public class QPaymentRepositoryImpl implements PaymentRepository {
    private final JPAQueryFactory queryFactory;

    private final QPaymentEntity qPayment = QPaymentEntity.paymentEntity;

    @Override
    public List<PaymentRequest> findByUser(String user) {
        return queryFactory
                .selectFrom(qPayment)
                .where(qPayment.user.eq(user))
                .orderBy(qPayment.createdAt.desc())
                .transform(groupBy(qPayment.id).as(Projections.constructor(PaymentRequest.class,
                        qPayment.user,
                        qPayment.awardId,
                        qPayment.paymentKey,
                        qPayment.amount,
                        qPayment.orderId
                        )));
    }
}
