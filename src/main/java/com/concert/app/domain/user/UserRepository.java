package com.concert.app.domain.user;

public interface UserRepository {

    Users findById(long userId);

    Users findByIdWithLock(long userId);
}
