package com.concert.app.interfaces.api.v1.concert.res;

import com.concert.app.domain.concert.dto.SelectSeatResult;
import com.concert.app.domain.concert.enums.SeatStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record SelectSeatRes(
        @Schema(description = "콘서트 좌석 Id", defaultValue = "1")
        long seatId,

        @Schema(description = "콘서트 좌석 번호", defaultValue = "1")
        int position,

        @Schema(description = "콘서트 좌석 가격", defaultValue = "50000")
        long amount,

        @Schema(description = "콘서트 좌석 상태", defaultValue = "AVAILABLE")
        SeatStatus seatStatus
) {
    public static List<SelectSeatRes> of(List<SelectSeatResult> seatList) {
        return seatList.stream()
                .map(seat -> new SelectSeatRes(
                        seat.seatId(),
                        seat.position(),
                        seat.amount(),
                        seat.seatStatus()
                )).collect(Collectors.toList());
    }
}
