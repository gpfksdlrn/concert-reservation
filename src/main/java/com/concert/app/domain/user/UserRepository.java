package com.concert.app.domain.user;

public interface UserRepository {

    User findById(long userId);

    User findByIdWithLock(long userId);
}
