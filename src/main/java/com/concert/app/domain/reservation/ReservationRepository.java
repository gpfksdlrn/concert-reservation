package com.concert.app.domain.reservation;

public interface ReservationRepository {

    void save(Reservation reservation);

    Reservation findById(long reservationId);
}
