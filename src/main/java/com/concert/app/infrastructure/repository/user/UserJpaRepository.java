package com.concert.app.infrastructure.repository.user;

import com.concert.app.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Query
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByIdWithLock(long userId);
}