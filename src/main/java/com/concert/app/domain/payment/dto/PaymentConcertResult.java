package com.concert.app.domain.payment.dto;

import com.concert.app.domain.queue.QueueStatus;
import com.concert.app.domain.reservation.ReservationStatus;

public record PaymentConcertResult (
        long paymentAmount,
        ReservationStatus seatStatus,
        QueueStatus queueStatus
){
}
