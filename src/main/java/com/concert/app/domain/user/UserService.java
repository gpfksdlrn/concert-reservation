package com.concert.app.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public long selectUserAmount(String token) {
        long userId = User.extractUserIdFromJwt(token);
        User user = userRepository.findById(userId);
        return user.getUserAmount();
    }

    @Transactional
    public Long chargeUserAmount(String token, Long amount) {
        long userId = User.extractUserIdFromJwt(token);
        User user = userRepository.findByIdWithLock(userId);
        user.addAmount(amount);
        //TODO: PaymentHistory 저장
        return user.getUserAmount();
    }
}
