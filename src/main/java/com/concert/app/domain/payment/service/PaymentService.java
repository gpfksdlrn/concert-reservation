package com.concert.app.domain.payment.service;

import com.concert.app.domain.concert.entity.ConcertSeat;
import com.concert.app.domain.concert.repository.ConcertSeatRepository;
import com.concert.app.domain.payment.dto.PaymentConcertResult;
import com.concert.app.domain.payment.entity.Payment;
import com.concert.app.domain.payment.entity.PaymentHistory;
import com.concert.app.domain.payment.enums.PaymentType;
import com.concert.app.domain.payment.repository.PaymentHistoryRepository;
import com.concert.app.domain.payment.repository.PaymentRepository;
import com.concert.app.domain.queue.Queue;
import com.concert.app.domain.queue.QueueRepository;
import com.concert.app.domain.reservation.Reservation;
import com.concert.app.domain.reservation.ReservationRepository;
import com.concert.app.domain.user.User;
import com.concert.app.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public PaymentConcertResult paymentConcert(String token, long reservationId) {
        long userId = User.extractUserIdFromJwt(token);
        User user = userRepository.findById(userId);

        Queue queue = queueRepository.findByToken(token);
        queue.tokenReserveCheck();

        Reservation reservation = reservationRepository.findById(reservationId);
        user.checkConcertAmount(reservation.getSeatAmount());

        // 낙관적 락을 사용하여 좌석 조회 및 예약 처리
        ConcertSeat concertSeat = concertSeatRepository.findById(reservation.getSeatId());
        concertSeat.finishSeatReserve();

        queue.finishQueue();
        reservation.finishReserve();

        Payment payment = paymentRepository.findByReservationId(reservation.getId());
        payment.finishPayment();

        PaymentHistory paymentHistory = PaymentHistory.enterPaymentHistory(userId, payment.getPrice(), PaymentType.PAYMENT, payment.getId());
        paymentHistoryRepository.save(paymentHistory);

        return new PaymentConcertResult(concertSeat.getAmount(), reservation.getStatus(), queue.getStatus());
    }
}
