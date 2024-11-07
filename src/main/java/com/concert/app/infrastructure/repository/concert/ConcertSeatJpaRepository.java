package com.concert.app.infrastructure.repository.concert;

import com.concert.app.domain.concert.dto.SelectSeatResult;
import com.concert.app.domain.concert.entity.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    @Query("""
    select new com.concert.app.domain.concert.dto.SelectSeatResult(
        cs.id,
        cs.position,
        cs.amount,
        cs.seatStatus
    )
    from
        ConcertSeat cs join ConcertSchedule csh on cs.concertScheduleId = csh.id
    where
        cs.id = :scheduleId
    and cs.isDelete = false
    and csh.isDelete = false
    and cs.seatStatus = "AVAILABLE"
    """)
    List<SelectSeatResult> findConcertSeat(
            @Param("scheduleId") long scheduleId
    );
}
