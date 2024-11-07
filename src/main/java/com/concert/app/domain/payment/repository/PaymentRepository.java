package com.concert.app.domain.payment.repository;

import com.concert.app.domain.payment.entity.Payment;

public interface PaymentRepository {
    void save(Payment payment);

    Payment findByReservationId(Long id);

}
