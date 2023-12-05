package de.bergmann.runnertracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "runs")
@AllArgsConstructor
@NoArgsConstructor
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @JoinColumn(name = "running_tracker_user_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = RunnerUser.class, fetch = FetchType.EAGER)
    @ToString.Exclude
    private RunnerUser runningUser;
    @Column(name = "running_tracker_user_id")
    private long runningUserId;
    @Column(name = "start_latitude")
    private double startLatitude;
    @Column(name = "start_longitude")
    private double startLongitude;
    @Column(name = "finish_latitude")
    private double finishLatitude;
    @Column(name = "finish_longitude")
    private double finishLongitude;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;
    @Column(name = "finish_datetime")
    private LocalDateTime finishDateTime;

}
