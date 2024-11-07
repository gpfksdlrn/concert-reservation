package com.concert.app.infrastructure.repository.payment;

import com.concert.app.domain.payment.entity.Payment;
import com.concert.app.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;

    @Override
    public void save(Payment payment) {
        jpaRepository.save(payment);
    }

    @Override
    public Payment findByReservationId(Long id) {
        return jpaRepository.findByReservationId(id).orElseThrow(
                () -> new NullPointerException("해당 예약 아이디의 결제 정보가 존재하지 않습니다.")
        );
    }
}
