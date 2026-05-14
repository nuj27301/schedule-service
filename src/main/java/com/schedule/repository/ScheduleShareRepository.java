package com.schedule.repository;

import com.schedule.entity.ScheduleShare;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ScheduleShareRepository extends JpaRepository<ScheduleShare, Long> {
    List<ScheduleShare> findByMemberId(Long memberId);
    boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId);
    List<ScheduleShare> findByScheduleId(Long scheduleId);
    Optional<ScheduleShare> findByScheduleIdAndMemberId(Long scheduleId, Long memberId);
}