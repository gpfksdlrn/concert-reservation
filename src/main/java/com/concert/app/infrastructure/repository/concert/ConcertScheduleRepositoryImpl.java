package com.concert.app.infrastructure.repository.concert;

import com.concert.app.domain.concert.dto.SelectConcertResult;
import com.concert.app.domain.concert.entity.ConcertSchedule;
import com.concert.app.domain.concert.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {
    private final ConcertScheduleJpaRepository jpaRepository;

    @Override
    public List<SelectConcertResult> findConcertSchedule() {
        return jpaRepository.findConcertSchedule();
    }

    @Override
    public ConcertSchedule findById(long scheduleId) {
        return jpaRepository.findById(scheduleId).orElseThrow(
                () -> new NullPointerException("해당 아이디를 가진 콘서트 스케줄이 존재하지 않습니다.")
        );
    }
}
