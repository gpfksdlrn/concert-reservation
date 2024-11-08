package com.concert.app.domain.user;

import com.concert.app.domain.payment.entity.PaymentHistory;
import com.concert.app.domain.payment.enums.PaymentType;
import com.concert.app.domain.payment.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional(readOnly = true)
    public long selectUserAmount(String token) {
        long userId = Users.extractUserIdFromJwt(token);
        Users users = userRepository.findById(userId);
        return users.getUserAmount();
    }

    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 200)
    )
    public Long chargeUserAmount(String token, Long amount) {
        long userId = Users.extractUserIdFromJwt(token);
        Users users = userRepository.findById(userId);
        users.addAmount(amount);
        PaymentHistory paymentHistory = PaymentHistory.enterPaymentHistory(users.getId(), amount, PaymentType.REFUND);
        paymentHistoryRepository.save(paymentHistory);
        return users.getUserAmount();
    }
}
