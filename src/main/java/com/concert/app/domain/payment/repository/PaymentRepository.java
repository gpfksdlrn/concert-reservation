package com.concert.app.domain.payment.repository;

import com.concert.app.domain.payment.entity.Payment;

import java.util.List;

public interface PaymentRepository {
    void save(Payment payment);

    Payment findByReservationId(Long id);

}
