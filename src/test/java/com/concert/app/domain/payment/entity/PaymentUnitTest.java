package com.concert.app.domain.payment.entity;

import com.concert.app.domain.payment.enums.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentUnitTest {
    @Test
    @DisplayName("결제를_진행한다.")
    void test1() {
        // given
        long userId = 1L;

        // when
        Payment payment = Payment.enterPayment(userId, 1L, 500, PaymentStatus.PROGRESS);

        // 결제 완료 처리
        payment.finishPayment();

        // then
        assertEquals(PaymentStatus.DONE, payment.getStatus());
        assertEquals(500L, payment.getPrice());
    }
}