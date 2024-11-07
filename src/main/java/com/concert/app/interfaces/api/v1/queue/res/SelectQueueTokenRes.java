package com.concert.app.interfaces.api.v1.queue.res;

import com.concert.app.domain.queue.QueueStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record SelectQueueTokenRes(
        @Schema(description = "대기열 순서", defaultValue = "30")
        long queuePosition,

        @Schema(description = "대기열 상태", defaultValue = "WAITING")
        QueueStatus status
) {
    public static SelectQueueTokenRes of(long queuePosition, QueueStatus status) {
        return new SelectQueueTokenRes(queuePosition, status);
    }
}
