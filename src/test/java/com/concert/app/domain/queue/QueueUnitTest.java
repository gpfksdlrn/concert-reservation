package com.concert.app.domain.queue;

import com.concert.app.interfaces.api.exception.ApiException;
import com.concert.app.interfaces.api.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueueUnitTest {
    @Nested
    class WaitingQueueTests {
        @Test
        @DisplayName("대기열이_30명_이하이면_PROGRESS_상태로_변경된다")
        void test1() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING,
                    LocalDateTime.now().plusMinutes(10));
            List<Queue> queueList = Collections.nCopies(30, queue); // 30명의 대기열

            // when
            queue.checkWaitingQueue(queueList);

            // then
            assertEquals(QueueStatus.PROGRESS, queue.getStatus());
        }

        @Test
        @DisplayName("대기열이_30명_초과이면_WAITING_상태를_유지한다")
        void test2() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING,
                    LocalDateTime.now().plusMinutes(10));
            List<Queue> queueList = Collections.nCopies(31, queue); // 31명의 대기열

            // when
            queue.checkWaitingQueue(queueList);

            // then
            assertEquals(QueueStatus.WAITING, queue.getStatus());
        }
    }

    @Nested
    class TokenValidationTests {
        @Test
        @DisplayName("WAITING_상태이고_만료시간이_없으면_토큰이_유효하다")
        void test1() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING, null);

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("WAITING_상태이고_만료시간이_남아있으면_토큰이_유효하다")
        void test2() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING,
                    LocalDateTime.now().plusMinutes(10));

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("WAITING_상태이고_만료시간이_지나면_토큰이_유효하지_않다")
        void test3() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING,
                    LocalDateTime.now().plusMinutes(10));

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("PROGRESS_상태이고_만료시간이_없으면_토큰이_유효하다")
        void test4() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.PROGRESS, null);

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertTrue(isValid);
        }

        @Test
        @DisplayName("PROGRESS_상태이고_만료시간이_지나면_토큰이_유효하지_않다")
        void test5() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.PROGRESS,
                    LocalDateTime.now().minusMinutes(10));

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertFalse(isValid);
        }

        @Test
        @DisplayName("EXPIRED_상태면_만료시간과_관계없이_토큰이_유효하지_않다")
        void test6() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.EXPIRED,
                    LocalDateTime.now().plusMinutes(10));

            // when
            boolean isValid = queue.isTokenValid();

            // then
            assertFalse(isValid);
        }

    }

    @Nested
    class InterceptorTests {

        @Test
        @DisplayName("정상_토큰은_검증을_통과한다")
        void test1() {
            // given
            String validToken = "test-token";

            // when & then
            assertDoesNotThrow(() -> Queue.tokenNullCheck(validToken));

        }

        @Test
        @DisplayName("빈_토큰은_IllegalArgumentException_이_발생한다")
        void test2() {
            // given
            String emptyToken = "";

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> Queue.tokenNullCheck(emptyToken));

            // then
            assertEquals("토큰이 존재하지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("PROGRESS가_아닌_상태의_토큰은_E403_예외가_발생한다")
        void test3() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.WAITING,
                    LocalDateTime.now().plusMinutes(10));

            // when
            ApiException exception = assertThrows(ApiException.class, queue::checkToken);

            // then
            assertEquals(ExceptionCode.E403, exception.getExceptionCode());
        }

        @Test
        @DisplayName("만료된_토큰은_E403_예외가_발생한다")
        void test4() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.PROGRESS,
                    LocalDateTime.now().minusMinutes(1));

            // when
            ApiException exception = assertThrows(ApiException.class, queue::checkToken);

            // then
            assertEquals(ExceptionCode.E403, exception.getExceptionCode());
        }

        @Test
        @DisplayName("PROGRESS_상태이고_유효시간_내의_토큰은_검증을_통과한다")
        void test5() {
            // given
            Queue queue = new Queue(1L, "test-token", QueueStatus.PROGRESS,
                    LocalDateTime.now().plusMinutes(10));

            // when & then
            assertDoesNotThrow(queue::checkToken);
        }
    }
}