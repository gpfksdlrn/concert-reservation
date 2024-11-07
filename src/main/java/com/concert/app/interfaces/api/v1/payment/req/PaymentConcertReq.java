package com.concert.app.interfaces.api.v1.payment.req;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentConcertReq(
        @Schema(description = "콘서트 예약 id", defaultValue = "1")
        long reservationId
) {
}
