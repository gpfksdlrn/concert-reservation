package com.concert.app.domain.concert.entity;

import com.concert.app.domain.concert.enums.SeatStatus;
import com.concert.app.domain.concert.enums.TotalSeatStatus;
import com.concert.app.interfaces.api.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConcertUnitTest {
    @Nested
    class ConcertScheduleTests {
        @Test
        @DisplayName("콘서트가_매진되어_예약할_수_없다")
        void test1() {
            // given
            ConcertSchedule concertSchedule = new ConcertSchedule(1L, 1L, LocalDate.now(), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3), 100, 0, TotalSeatStatus.SOLD_OUT, LocalDateTime.now(), false);

            // when & then
            assertThrows(ApiException.class, concertSchedule::isSoldOutCheck);
        }
    }

    @Nested
    class ConcertSeatTests {
        @Test
        @DisplayName("좌석이_AVAILABLE_상태일_때_예약_가능하다")
        void test1() {
            // given
            ConcertSeat concertSeat = new ConcertSeat(1L, 1L, 500L, 10, SeatStatus.AVAILABLE, null, LocalDateTime.now(), false);

            // when & then : 예약 가능
            assertDoesNotThrow(concertSeat::isReserveCheck);
        }

        @Test
        @DisplayName("좌석이_RESERVED_상태일_때_예약_불가능하다")
        void test2() {
            // given
            ConcertSeat concertSeat = new ConcertSeat(1L, 1L, 500L, 10, SeatStatus.RESERVED, null, LocalDateTime.now(), false);

            // when & then
            assertThrows(ApiException.class, concertSeat::isReserveCheck, "해당 좌석은 예약할 수 없는 상태입니다.");
        }

        @Test
        @DisplayName("좌석이_TEMP_RESERVED_상태일_때_예약_불가능하다")
        void test3() {
            // given
            ConcertSeat concertSeat = new ConcertSeat(1L, 1L, 500L, 10, SeatStatus.TEMP_RESERVED, null, LocalDateTime.now(), false);

            // when & then
            assertThrows(ApiException.class, concertSeat::isReserveCheck, "해당 좌석은 예약할 수 없는 상태입니다.");
        }
    }
}