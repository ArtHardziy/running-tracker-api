package de.bergmann.runnertracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {
    private int numberOfRuns;
    private double totalDistance;
    private double averageSpeed;

    private RunnerUserDTO runner;
}
