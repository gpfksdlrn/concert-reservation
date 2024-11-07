package com.concert.app.infrastructure.repository.concert;

import com.concert.app.domain.concert.dto.SelectConcertResult;
import com.concert.app.domain.concert.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    @Query("""
    select new com.concert.app.domain.concert.dto.SelectConcertResult(
        cs.id,
        c.title,
        cs.openDt,
        cs.startDt,
        cs.endDt,
        cs.totalSeatStatus
    )
    from
        ConcertSchedule cs join Concert c on cs.concertId = c.id
    where
        cs.startDt >= current_timestamp
    and
        cs.totalSeatStatus = "AVAILABLE"
    and
        cs.isDelete = false
    and
        c.isDelete = false
    """)
    List<SelectConcertResult> findConcertSchedule();
}
