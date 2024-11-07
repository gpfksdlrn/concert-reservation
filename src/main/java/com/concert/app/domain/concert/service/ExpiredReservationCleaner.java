package com.concert.app.domain.concert.service;

import com.concert.app.domain.concert.repository.ConcertRepository;
import com.concert.app.domain.payment.enums.PaymentStatus;
import com.concert.app.domain.reservation.Reservation;
import com.concert.app.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ExpiredReservationCleaner {

    private final ConcertRepository concertRepository;

    public void release() {
        LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(5);
        List<Reservation> reservations = concertRepository.findReservationReleaseTarget(expiredAt, ReservationStatus.TEMP_RESERVED, PaymentStatus.DONE);
        concertRepository.deleteReservation(reservations);
        concertRepository.deletePayment(getReservationIds(reservations));
        concertRepository.deleteSeats(getSeatIds(reservations));
    }

    private List<Long> getReservationIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getId).toList();
    }

    private List<Long> getSeatIds(List<Reservation> reservations) {
        return reservations.stream().map(Reservation::getSeatId).toList();
    }
}
