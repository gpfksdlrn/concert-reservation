package com.concert.app.infrastructure.repository.concert;

import com.concert.app.domain.concert.dto.SelectSeatResult;
import com.concert.app.domain.concert.entity.ConcertSeat;
import com.concert.app.domain.concert.repository.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {

    private final ConcertSeatJpaRepository jpaRepository;

    @Override
    public List<SelectSeatResult> findConcertSeat(long scheduleId) {
        return jpaRepository.findConcertSeat(scheduleId);
    }

    @Override
    public ConcertSeat findById(long seatId) {
        return jpaRepository.findById(seatId).orElseThrow(
            () -> new RuntimeException("해당 정보를 가진 좌석을 조회할 수 있습니다.")
        );
    }

    @Override
    public void save(ConcertSeat concertSeat) {
        jpaRepository.save(concertSeat);
    }
}
