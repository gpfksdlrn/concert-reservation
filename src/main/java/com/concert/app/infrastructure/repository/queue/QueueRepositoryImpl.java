package com.concert.app.infrastructure.repository.queue;

import com.concert.app.domain.queue.Queue;
import com.concert.app.domain.queue.QueueRepository;
import com.concert.app.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository jpaRepository;

    @Override
    public Queue findByUserIdForWaitingOrProgress(Long userId) {
        return jpaRepository.findByUserIdForWaitingOrProgress(userId)
                .orElse(null);
    }

    @Override
    public void save(Queue queue) {
        jpaRepository.save(queue);
    }

    @Override
    public List<Queue> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Queue> findOrderByDescByStatus(QueueStatus queueStatus) {
        return jpaRepository.findAllByStatusOrderByIdDesc(queueStatus);
    }

    @Override
    public Queue findByToken(String token) {
        return jpaRepository.findByToken(token)
                .orElseThrow(() -> new NullPointerException("해당 토큰의 큐가 존재하지 않습니다."));
    }

    @Override
    public Long findStatusIsWaitingAndAlreadyEnteredBy(LocalDateTime enteredDt, QueueStatus queueStatus) {
        return jpaRepository.findStatusIsWaitingAndAlreadyEnteredBy(enteredDt, queueStatus);
    }

    @Override
    public void updateExpireConditionToken() {
        jpaRepository.updateStatusExpire(
                QueueStatus.EXPIRED,
                QueueStatus.PROGRESS,
                LocalDateTime.now()
        );
    }

    @Override
    public int countByStatus(QueueStatus queueStatus) {
        return jpaRepository.countByStatus(queueStatus);
    }

    @Override
    public List<Queue> findTopWaiting(int remainingSlots) {
        return jpaRepository.findTopWaiting(QueueStatus.WAITING, remainingSlots);
    }

    @Override
    public void updateStatusByIds(List<Long> collect, QueueStatus queueStatus) {
        jpaRepository.updateStatusByIds(collect, queueStatus);
    }
}
