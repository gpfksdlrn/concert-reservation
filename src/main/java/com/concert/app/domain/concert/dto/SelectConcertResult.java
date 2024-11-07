package com.concert.app.domain.concert.dto;

import com.concert.app.domain.concert.enums.TotalSeatStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SelectConcertResult(
        long scheduleId,
        String concertTitle,
        LocalDate openDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        TotalSeatStatus seatStatus
) {
}
