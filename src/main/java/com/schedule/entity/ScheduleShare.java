package com.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_shares")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ShareRole role;

    private LocalDateTime sharedAt;

    @PrePersist
    public void prePersist() {
        this.sharedAt = LocalDateTime.now();
    }

    public enum ShareRole {
        VIEWER, EDITOR
    }
}