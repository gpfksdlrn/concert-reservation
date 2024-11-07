package com.concert.app.infrastructure.repository.concert;

import com.concert.app.domain.concert.entity.Concert;
import com.concert.app.domain.concert.repository.ConcertRepository;
import com.concert.app.domain.payment.enums.PaymentStatus;
import com.concert.app.domain.reservation.Reservation;
import com.concert.app.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertJpaRepository jpaRepository;

    @Override
    public Concert findById(Long concertId) {
        return jpaRepository.findById(concertId).orElseThrow(
                () -> new NullPointerException("해당 아이디를 가진 콘서트가 존재하지 않습니다."));
    }

    @Override
    public void save(Concert concert) {
        jpaRepository.save(concert);
    }


    @Override
    public List<Reservation> findReservationReleaseTarget(LocalDateTime expiredAt, ReservationStatus reservationStatus, PaymentStatus paymentStatus) {
        return jpaRepository.findReservationReleaseTarget(expiredAt, reservationStatus, paymentStatus);
    }

    @Override
    public void deleteReservation(List<Reservation> reservations) {
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public void deletePayment(List<Long> reservationIds) {
        jpaRepository.deleteAllById(reservationIds);
    }

    @Override
    public void deleteSeats(List<Long> seatIds) {
        jpaRepository.deleteAllById(seatIds);
    }
}
