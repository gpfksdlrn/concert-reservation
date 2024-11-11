package com.concert.app.domain.user;

public interface UserRepository {

    Users findById(long userId);

    void save(Users user);
}
