package com.concert.app.infrastructure.repository.payment;

import com.concert.app.domain.payment.entity.PaymentHistory;
import com.concert.app.domain.payment.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
    private final PaymentHistoryJpaRepository jpaRepository;

    @Override
    public void save(PaymentHistory paymentHistory) {
        jpaRepository.save(paymentHistory);
    }
}
