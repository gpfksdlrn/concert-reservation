package com.concert.app.interfaces.api.v1.payment.res;

import com.concert.app.domain.payment.dto.PaymentConcertResult;
import com.concert.app.domain.queue.QueueStatus;
import com.concert.app.domain.reservation.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentConcertRes(
        @Schema(description = "지불된 요금", defaultValue = "50000")
        long paymentAmount,

        @Schema(description = "콘서트 좌석 상태", defaultValue = "RESERVED")
        ReservationStatus seatStatus,

        @Schema(description = "예약 대기상태(큐상태)", defaultValue = "DONE")
        QueueStatus queueStatus
) {
    public static PaymentConcertRes of(PaymentConcertResult paymentConcertResult) {
        return new PaymentConcertRes(
                paymentConcertResult.paymentAmount(),
                paymentConcertResult.seatStatus(),
                paymentConcertResult.queueStatus()
        );
    }
}
