package com.concert.app.domain.queue;

public record SelectQueueTokenResult(
        long queuePosition,
        QueueStatus status
) {
}
