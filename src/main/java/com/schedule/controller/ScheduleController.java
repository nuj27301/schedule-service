package com.schedule.controller;

import com.schedule.dto.ScheduleDto;
import com.schedule.entity.Schedule;
import com.schedule.entity.ScheduleShare;
import com.schedule.service.EmailService;
import com.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final EmailService emailService;

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails user, Model model) {
        List<Schedule> schedules = scheduleService.getMySchedules(user.getUsername());
        List<Schedule> sharedSchedules = scheduleService.getSharedSchedules(user.getUsername());
        
        Map<Long, ScheduleShare.ShareRole> shareRoles = new HashMap<>();
        sharedSchedules.forEach(schedule -> {
            ScheduleShare.ShareRole role = scheduleService.getShareRole(schedule.getId(), user.getUsername());
            shareRoles.put(schedule.getId(), role);
        });
        
        model.addAttribute("schedules", schedules);
        model.addAttribute("sharedSchedules", sharedSchedules);
        model.addAttribute("shareRoles", shareRoles);
        return "schedule/list";
    }
    
    @GetMapping("/new")
    public String newForm() {
        return "schedule/form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute ScheduleDto dto,
                         @AuthenticationPrincipal UserDetails user) {
        scheduleService.create(dto, user.getUsername());
        return "redirect:/schedule";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("schedule", scheduleService.getSchedule(id));
        return "schedule/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute ScheduleDto dto) {
        scheduleService.update(id, dto);
        return "redirect:/schedule";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return "redirect:/schedule";
    }

    @PostMapping("/{id}/share")
    public String share(@PathVariable Long id,
                        @RequestParam String email,
                        @RequestParam String role,
                        @AuthenticationPrincipal UserDetails user) {
        Schedule schedule = scheduleService.getSchedule(id);
        scheduleService.share(id, email, ScheduleShare.ShareRole.valueOf(role));
        emailService.sendShareNotification(email, schedule.getTitle(), user.getUsername());
        return "redirect:/schedule";
    }
}