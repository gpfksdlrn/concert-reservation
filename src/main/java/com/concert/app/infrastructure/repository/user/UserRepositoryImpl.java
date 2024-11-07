package com.concert.app.infrastructure.repository.user;

import com.concert.app.domain.user.User;
import com.concert.app.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;;

    @Override
    public User findById(long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("해당 id의 유저가 존재하지 않습니다."));
    }

    @Override
    public User findByIdWithLock(long userId) {
        return userJpaRepository.findByIdWithLock(userId)
                .orElseThrow(() -> new NullPointerException("해당 id의 유저가 존재하지 않습니다."));
    }
}
