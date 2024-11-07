package com.concert.app.domain.concert.repository;

import com.concert.app.domain.concert.dto.SelectSeatResult;
import com.concert.app.domain.concert.entity.ConcertSeat;

import java.util.List;

public interface ConcertSeatRepository {
    List<SelectSeatResult> findConcertSeat(long scheduleId);

    ConcertSeat findById(long seatId);
}
