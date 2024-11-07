package com.concert.app.domain.queue;

import java.time.LocalDateTime;
import java.util.List;

public interface QueueRepository {
    List<Queue> findAll();

    Queue findByToken(String token);

    Queue findByUserIdForWaitingOrProgress(Long userId);

    void save(Queue queue);

    List<Queue> findOrderByDescByStatus(QueueStatus queueStatus);

    Long findStatusIsWaitingAndAlreadyEnteredBy(LocalDateTime enteredDt, QueueStatus queueStatus);

    void updateExpireConditionToken();

    int countByStatus(QueueStatus queueStatus);

    List<Queue> findTopWaiting(int remainingSlots);

    void updateStatusByIds(List<Long> collect, QueueStatus queueStatus);
}
