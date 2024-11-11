package com.concert.app.domain.reservation;

import com.concert.IntegrationTest;
import com.concert.app.domain.concert.entity.Concert;
import com.concert.app.domain.concert.entity.ConcertSchedule;
import com.concert.app.domain.concert.entity.ConcertSeat;
import com.concert.app.domain.concert.enums.SeatStatus;
import com.concert.app.domain.concert.enums.TotalSeatStatus;
import com.concert.app.domain.concert.repository.ConcertRepository;
import com.concert.app.domain.concert.repository.ConcertScheduleRepository;
import com.concert.app.domain.concert.repository.ConcertSeatRepository;
import com.concert.app.domain.queue.Queue;
import com.concert.app.domain.queue.QueueRepository;
import com.concert.app.domain.queue.QueueStatus;
import com.concert.app.domain.user.UserRepository;
import com.concert.app.domain.user.Users;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationServiceTest extends IntegrationTest {
    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertScheduleRepository concertScheduleRepository;

    @Autowired
    private ConcertSeatRepository concertSeatRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationService reservationService;

    @Nested
    class ConcurrencyTests {
        @Test
        @DisplayName("동시에_100개의_요청이_들어올때_하나의_좌석은_한번만_예약된다")
        void test1() throws InterruptedException {
            // given
            int numberOfThreads = 100;
            ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            Users user = new Users(1L, 1000L);
            userRepository.save(user);
            String token = "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjEsInRva2VuIjoiMzc2NzcxMTctNzZjMy00NjdjLWFmMjEtOTY0ODI3Nzc3YTU3IiwiZW50ZXJlZER0IjoxNzI5MDY3NjIxMTIwLCJleHBpcmVkRHQiOjE3MjkwNjk0MjExMjB9.";
            Queue queue = new Queue(user.getId(), token, QueueStatus.PROGRESS, null);
            queueRepository.save(queue);

            // 콘서트, 콘서트 스케줄 및 좌석 설정
            Concert concert = new Concert(1L, "testConcert", LocalDateTime.now(), false);
            concertRepository.save(concert);
            ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert.getId(), LocalDateTime.now().toLocalDate(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 50, 50, TotalSeatStatus.AVAILABLE, LocalDateTime.now(), false);
            concertScheduleRepository.save(concertSchedule);
            ConcertSeat concertSeat = new ConcertSeat(1L, concertSchedule.getId(), 500L, 1, SeatStatus.AVAILABLE, null, LocalDateTime.now(), false);
            concertSeatRepository.save(concertSeat);

            for (int i = 0; i < numberOfThreads; i++) {
                service.execute(() -> {
                   try {
                        // when
                       reservationService.reserveConcert(token, concertSchedule.getId(), concertSeat.getId());
                   } finally {
                       latch.countDown();
                   }
                });
            }
            latch.await();

            // then
            List<Reservation> reservations = reservationRepository.findAll();
            AssertionsForInterfaceTypes.assertThat(reservations).hasSize(1);
        }

        @Test
        @DisplayName("동시에_5명의_유저의_동일_좌석을_예약하려고_할_때_한번만_정상적으로_처리된다")
        void test2() throws InterruptedException {
            // given
            // 5명의 유저 생성 및 저장
            List<Users> userList = LongStream.rangeClosed(1, 5)
                    .mapToObj(id -> new Users(id, 1000L))
                    .toList();

            userList.forEach(userRepository::save);

            List<String> tokens = List.of(
                    "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjEsInRva2VuIjoiMzc2NzcxMTctNzZjMy00NjdjLWFmMjEtOTY0ODI3Nzc3YTU3IiwiZW50ZXJlZER0IjoxNzI5MDY3NjIxMTIwLCJleHBpcmVkRHQiOjE3MjkwNjk0MjExMjB9.",
                    "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjIsInRva2VuIjoiNTQ0ODAyMjUtZmI4Mi00OTRlLThkZDQtOGE1NTYzYjBlN2EwIiwiZW50ZXJlZER0IjoxNzI5MTg3NzE4NzIyLCJleHBpcmVkRHQiOjE3MjkxODgwMTg3MjJ9.",
                    "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjMsInRva2VuIjoiOWUyMTk1NWMtMjY5YS00ZDllLWFmYzYtOTljOTE1NTg4NTFiIiwiZW50ZXJlZER0IjoxNzI5MTg3ODM1MzgzLCJleHBpcmVkRHQiOjE3MjkxODgxMzUzODN9.",
                    "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjQsInRva2VuIjoiNTRiOTdiNmEtNjJjMS00NDg0LTg4NjgtNzc5OTUxYTA4YTBjIiwiZW50ZXJlZER0IjoxNzI5MTg3ODQ5MzMwLCJleHBpcmVkRHQiOjE3MjkxODgxNDkzMzB9.",
                    "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjUsInRva2VuIjoiODg5ZGI4OGQtNzVlZS00MTFlLWI3MzAtOTg3NmIyYTk0MWFhIiwiZW50ZXJlZER0IjoxNzI5MTg3ODY3OTExLCJleHBpcmVkRHQiOjE3MjkxODgxNjc5MTF9."
            );

            List<Queue> queueList = LongStream.rangeClosed(1, 5)
                    .mapToObj(id -> new Queue(id, tokens.get((int) id - 1), QueueStatus.PROGRESS, LocalDateTime.now().plusMinutes(10)))
                    .toList();

            queueList.forEach(queueRepository::save);

            // 콘서트와 좌석 설정 및 저장
            Concert concert = new Concert(1L, "testConcert", LocalDateTime.now(), false);
            concertRepository.save(concert);
            ConcertSchedule concertSchedule = new ConcertSchedule(
                    1L, concert.getId(), LocalDate.now(), LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(5),
                    50, 50, TotalSeatStatus.AVAILABLE, LocalDateTime.now(), false
            );
            concertScheduleRepository.save(concertSchedule);
            ConcertSeat concertSeat = new ConcertSeat(
                    1L, concertSchedule.getId(), 500L, 1, SeatStatus.AVAILABLE, null, LocalDateTime.now(), false
            );
            concertSeatRepository.save(concertSeat);

            // 동시에 제어를 위한 ExecutorService 설정
            int threadCount = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            // when
            tokens.forEach(
                token -> executorService.submit(() -> {
                    try {
                        reservationService.reserveConcert(token, concertSchedule.getId(), concertSeat.getId());
                    } finally {
                        latch.countDown();
                    }
                })
            );

            latch.await(); // 모든 스레드가 완료될 때까지 대기

            // then
            ConcertSeat updatedSeat = concertSeatRepository.findById(concertSeat.getId());
            List<Reservation> reservations = reservationRepository.findAll();

            // 하나의 스레드만 성공적으로 좌석을 예약해야 함
            assertThat(reservations).hasSize(1);
            assertThat(updatedSeat.getSeatStatus()).isEqualTo(SeatStatus.TEMP_RESERVED); // 좌석이 예약된 상태로 변경
        }
    }
}