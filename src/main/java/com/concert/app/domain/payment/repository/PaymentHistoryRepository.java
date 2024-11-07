package com.concert.app.domain.payment.repository;

import com.concert.app.domain.payment.entity.PaymentHistory;

public interface PaymentHistoryRepository {
    void save(PaymentHistory paymentHistory);
}
