package com.concert.app.domain.concert.repository;

import com.concert.app.domain.concert.entity.Concert;
import com.concert.app.domain.payment.enums.PaymentStatus;
import com.concert.app.domain.reservation.Reservation;
import com.concert.app.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {
    Concert findById(Long concertId);
    void save(Concert concert);
    List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt, ReservationStatus reservationStatus, PaymentStatus paymentStatus);
    void deleteReservation(List<Reservation> reservations);
    void deletePayment(List<Long> reservationIds);
    void deleteSeats(List<Long> seatIds);
}
