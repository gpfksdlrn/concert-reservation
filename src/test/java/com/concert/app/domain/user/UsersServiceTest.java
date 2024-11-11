package com.concert.app.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import com.concert.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.CompletableFuture;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UsersServiceTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final String TEST_TOKEN = "eyJhbGciOiJub25lIn0.eyJ1c2VySWQiOjEsInRva2VuIjoiMzc2NzcxMTctNzZjMy00NjdjLWFmMjEtOTY0ODI3Nzc3YTU3IiwiZW50ZXJlZER0IjoxNzI5MDY3NjIxMTIwLCJleHBpcmVkRHQiOjE3MjkwNjk0MjExMjB9.";

    @Nested
    class ConcurrencyTests {
        @Test
        @DisplayName("잔액_1만원인_유저가_1000원_2000원_3000원을_동시에_충전하면_16000원이_된다.")
        void test1() throws InterruptedException {
            /// given
            Users user = new Users(1L, 10000L);
            userRepository.save(user);

            // when: 1000원, 2000원, 3000원 동시 충전
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(() -> userService.chargeUserAmount(TEST_TOKEN, 1000L)),
                    CompletableFuture.runAsync(() -> userService.chargeUserAmount(TEST_TOKEN, 2000L)),
                    CompletableFuture.runAsync(() -> userService.chargeUserAmount(TEST_TOKEN, 3000L))
            ).join();
            Thread.sleep(100L);

            // then: 10000 + 1000 + 2000 + 3000 = 16000
            long amount = userService.selectUserAmount(TEST_TOKEN);
            assertThat(amount).isEqualTo(16000L);
        }

        @Test
        @DisplayName("잔액_1만원인_유저가_1000원씩_100번_동시에_충전요청을_보낸후_잔액은_101000원이_된다.")
        void test2() throws InterruptedException {
            // given
            Users user = new Users(1L, 10000L);
            userRepository.save(user);

            final int numberOfRequests = 100;
            final Long chargeAmount = 1000L;

            // when : 100번의 동시 충전 요청 실행
            CompletableFuture<Void>[] futures = new CompletableFuture[numberOfRequests];
            for (int i = 0; i < numberOfRequests; i++) {
                futures[i] = CompletableFuture.runAsync(() -> userService.chargeUserAmount(TEST_TOKEN, chargeAmount));
            }
            CompletableFuture.allOf(futures).join();

            // then : 10000 + (1000 * 100) = 110000
            Thread.sleep(100L);
            Users updatedUser = userRepository.findById(1L);
            assertThat(updatedUser.getUserAmount()).isEqualTo(110000L);
        }
    }

    @Nested
    class UserAmountTests {
        @Test
        @DisplayName("잔액이_1000원인_유저가_1000원과_2000원을_충전하면_4000원이_된다.")
        void test() {
            // given : 초기 잔액 1000원
            userRepository.save(new Users(1L, 1000L));

            // when : 1000원, 2000원 순차 충전
            userService.chargeUserAmount(TEST_TOKEN, 1000L);
            userService.chargeUserAmount(TEST_TOKEN, 2000L);
            long userAmount = userService.selectUserAmount(TEST_TOKEN);

            // then : 1000 + 1000 + 2000 = 4000
            assertThat(userAmount).isEqualTo(4000L);
        }
    }
}