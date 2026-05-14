package com.schedule.service;

import com.schedule.dto.ScheduleDto;
import com.schedule.entity.Member;
import com.schedule.entity.Schedule;
import com.schedule.entity.ScheduleShare;
import com.schedule.repository.MemberRepository;
import com.schedule.repository.ScheduleRepository;
import com.schedule.repository.ScheduleShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ScheduleShareRepository scheduleShareRepository;

    public Schedule create(ScheduleDto dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .member(member)
                .build();
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getMySchedules(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return scheduleRepository.findByMemberIdOrderByStartDateAsc(member.getId());
    }

    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정 없음"));
    }

    @Transactional
    public void update(Long id, ScheduleDto dto) {
        Schedule schedule = getSchedule(id);
        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setStatus(Schedule.ScheduleStatus.valueOf(dto.getStatus()));
    }

    @Transactional
    public void delete(Long id) {
        List<ScheduleShare> shares = scheduleShareRepository.findByScheduleId(id);
        scheduleShareRepository.deleteAll(shares);
        scheduleRepository.deleteById(id);
    }

    @Transactional
    public void share(Long scheduleId, String email, ScheduleShare.ShareRole role) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        if (scheduleShareRepository.existsByScheduleIdAndMemberId(scheduleId, member.getId())) {
            throw new RuntimeException("이미 공유된 회원입니다.");
        }
        Schedule schedule = getSchedule(scheduleId);
        ScheduleShare share = ScheduleShare.builder()
                .schedule(schedule)
                .member(member)
                .role(role)
                .build();
        scheduleShareRepository.save(share);
    }
    
    // 공유받은 일정 목록
    public List<Schedule> getSharedSchedules(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return scheduleShareRepository.findByMemberId(member.getId())
                .stream()
                .map(ScheduleShare::getSchedule)
                .filter(schedule -> !schedule.getMember().getId().equals(member.getId()))
                .toList();
    }
    
    public ScheduleShare.ShareRole getShareRole(Long scheduleId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return scheduleShareRepository.findByScheduleIdAndMemberId(scheduleId, member.getId())
                .map(ScheduleShare::getRole)
                .orElse(null);
    }
}