package com.concert.app.domain.concert.service;

import com.concert.app.domain.concert.dto.SelectConcertResult;
import com.concert.app.domain.concert.dto.SelectSeatResult;
import com.concert.app.domain.concert.repository.ConcertScheduleRepository;
import com.concert.app.domain.concert.repository.ConcertSeatRepository;
import com.concert.app.domain.user.User;
import com.concert.app.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertSeatRepository concertSeatRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SelectConcertResult> selectConcertList(String token) {
        long userId = User.extractUserIdFromJwt(token);
        userRepository.findById(userId);
        return concertScheduleRepository.findConcertSchedule();
    }

    @Transactional(readOnly = true)
    public List<SelectSeatResult> selectConcertSeatList(String token, long scheduleId) {
        long userId = User.extractUserIdFromJwt(token);
        userRepository.findById(userId);
        return concertSeatRepository.findConcertSeat(scheduleId);
    }
}
