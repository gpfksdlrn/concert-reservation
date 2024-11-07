package com.concert.app.infrastructure.repository.payment;

import com.concert.app.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.reservationId = :reservationId ORDER BY p.id DESC LIMIT 1")
    Optional<Payment> findByReservationId(
            @Param("reservationId") Long reservationId
    );
}
