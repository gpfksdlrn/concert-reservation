package com.concert.app.infrastructure.repository.reservation;

import com.concert.app.domain.reservation.Reservation;
import com.concert.app.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
    private final ReservationJpaRepository jpaRepository;

    @Override
    public void save(Reservation reservation) {
        jpaRepository.save(reservation);
    }

    @Override
    public Reservation findById(long reservationId) {
        return jpaRepository.findById(reservationId).orElseThrow(
                () -> new NullPointerException("해당 예약 정보가 존재하지 않습니다.")
        );
    }
}
