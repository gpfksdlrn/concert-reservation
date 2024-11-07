package com.concert.app.domain.concert.dto;

import com.concert.app.domain.concert.enums.SeatStatus;

public record SelectSeatResult(
        long seatId,
        int position,
        long amount,
        SeatStatus seatStatus
) {
}
