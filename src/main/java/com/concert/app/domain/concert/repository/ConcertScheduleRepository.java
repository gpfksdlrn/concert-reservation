package com.concert.app.domain.concert.repository;

import com.concert.app.domain.concert.dto.SelectConcertResult;
import com.concert.app.domain.concert.entity.ConcertSchedule;

import java.util.List;

public interface ConcertScheduleRepository {
    List<SelectConcertResult> findConcertSchedule();

    ConcertSchedule findById(long scheduleId);

    void save(ConcertSchedule concertSchedule);
}
