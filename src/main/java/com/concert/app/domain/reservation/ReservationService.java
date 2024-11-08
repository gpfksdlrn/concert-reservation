package com.concert.app.domain.reservation;

import com.concert.app.domain.concert.entity.Concert;
import com.concert.app.domain.concert.entity.ConcertSchedule;
import com.concert.app.domain.concert.entity.ConcertSeat;
import com.concert.app.domain.concert.repository.ConcertRepository;
import com.concert.app.domain.concert.repository.ConcertScheduleRepository;
import com.concert.app.domain.concert.repository.ConcertSeatRepository;
import com.concert.app.domain.payment.entity.Payment;
import com.concert.app.domain.payment.enums.PaymentStatus;
import com.concert.app.domain.payment.repository.PaymentRepository;
import com.concert.app.domain.queue.Queue;
import com.concert.app.domain.queue.QueueRepository;
import com.concert.app.domain.user.Users;
import com.concert.app.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final ConcertRepository concertRepository;
    private final QueueRepository queueRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public ReserveConcertResult reserveConcert(String token, long scheduleId, long seatId) {
        long userId = Users.extractUserIdFromJwt(token);
        Users users = userRepository.findById(userId);

        Queue queue = queueRepository.findByToken(token);
        queue.tokenReserveCheck();

        ConcertSchedule concertSchedule = concertScheduleRepository.findById(scheduleId);
        concertSchedule.isSoldOutCheck();

        // 낙관적 락을 사용하여 좌석 조회 및 예약 처리
        ConcertSeat concertSeat = concertSeatRepository.findById(seatId);
        concertSeat.isReserveCheck();

        Concert concert = concertRepository.findById(concertSchedule.getConcertId());
        Reservation reservation = Reservation.enterReservation(users, concert, concertSeat, concertSchedule);
        reservationRepository.save(reservation);

        Payment payment = Payment.enterPayment(userId, reservation.getId(), concertSeat.getAmount(), PaymentStatus.PROGRESS);
        paymentRepository.save(payment);

        return new ReserveConcertResult(reservation.getId(), reservation.getStatus(), reservation.getReservedDt(), reservation.getReservedUntilDt());
    }
}
