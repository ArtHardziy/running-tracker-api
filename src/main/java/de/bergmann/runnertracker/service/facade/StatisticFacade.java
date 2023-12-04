package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.model.dto.StatisticDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class StatisticFacade {

    private RunnerUserFacade userFacade;
    private RunsFacade runsFacade;

    public StatisticDto getStatisticByUserIdAndBetweenDates(Long userId, LocalDate fromDate, LocalDate toDate) {
        List<RunDto> runsByUserIdBetweenDates = runsFacade.findRunsByUserIdBetweenDates(userId, fromDate, toDate);
        var user = userFacade.findById(userId);
        return StatisticDto.builder()
                .runner(user)
                .numberOfRuns(runsByUserIdBetweenDates.size())
                .totalDistance(
                        runsByUserIdBetweenDates.stream()
                                .map(RunDto::getDistance)
                                .reduce((double) 0, Double::sum)
                )
                .averageSpeed(calculateAverageSpeedByRuns(runsByUserIdBetweenDates))
                .build();
    }

    private double calculateAverageSpeedByRuns(List<RunDto> runs) {
        var entireDistance = runs.stream()
                .map(RunDto::getDistance)
                .reduce((double) 0, Double::sum);
        var entireTime = runs.stream()
                .map(RunDto::getDurationInSec)
                .reduce((double) 0, Double::sum);

        return entireDistance / entireTime;
    }

}