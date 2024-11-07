package com.concert.app.interfaces.api.v1.reservation;

import com.concert.app.domain.reservation.ReservationService;
import com.concert.app.domain.reservation.ReserveConcertResult;
import com.concert.app.interfaces.api.common.CommonRes;
import com.concert.app.interfaces.api.v1.reservation.req.ReserveConcertReq;
import com.concert.app.interfaces.api.v1.reservation.res.ReserveConcertRes;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예약 API", description = "콘서트 예약 관련된 API 입니다. 모든 API는 대기열 토큰 헤더(Authorization)가 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/concerts")
public class ReservationController {
    private final ReservationService reservationService;

    public CommonRes<ReserveConcertRes> reserveConcert(
            @Schema(description = "대기열 토큰", defaultValue = "Bearer...") @RequestHeader("Authorization") String token,
            @RequestBody ReserveConcertReq req
    ) {
        ReserveConcertResult reserveResult = reservationService.reserveConcert(token, req.scheduleId(), req.seatId());
        return CommonRes.success(ReserveConcertRes.of(reserveResult));
    }
}
