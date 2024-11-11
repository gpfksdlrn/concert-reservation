package com.concert.app.domain.reservation;

import java.util.List;

public interface ReservationRepository {

    void save(Reservation reservation);

    Reservation findById(long reservationId);

    List<Reservation> findAll();
}
