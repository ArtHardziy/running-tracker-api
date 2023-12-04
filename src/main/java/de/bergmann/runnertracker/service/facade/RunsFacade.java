package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.Run;
import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.service.RunsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RunsFacade {

    private RunsService runsService;
    private static final double EARTH_RADIUS = 6378137.0;

    private RunDto convertToRunDto(Run run) {
        double runTimeInSec = calculateRunTimeInSec(run);
        double distance = calculateDistance(run);
        return RunDto.builder()
                .id(run.getId())
                .userId(run.getRunningUserId())
                .starDateTime(run.getStartDateTime())
                .finishDateTime(run.getFinishDateTime())
                .durationInSec(runTimeInSec)
                .distance(distance)
                .averageSpeed(distance / runTimeInSec)
                .build();
    }

    private double calculateDistance(Run run) {
        final double lat1Rad = Math.toRadians(run.getStartLongitude());
        final double lat2Rad = Math.toRadians(run.getFinishLongitude());
        final double lon1Rad = Math.toRadians(run.getStartLatitude());
        final double lon2Rad = Math.toRadians(run.getFinishLatitude());

        final double sinDLat = Math.sin((lat2Rad - lat1Rad) / 2);
        final double sinDLng = Math.sin((lon2Rad - lon1Rad) / 2);

        final double a = sinDLat * sinDLat + sinDLng * sinDLng * Math.cos(lat1Rad) * Math.cos(lat2Rad);
        return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private double calculateRunTimeInSec(Run run) {
        LocalDateTime startDateTime = run.getStartDateTime();
        LocalDateTime finishDateTime = run.getFinishDateTime();
        Duration duration = Duration.between(startDateTime, finishDateTime);
        return (double) duration.getSeconds();
    }

    public List<RunDto> findRunsByUserIdBetweenDates(Long id, LocalDate fromDateTime, LocalDate toDateTime) {
        return runsService.findRunsByUserIdBetweenDates(id, fromDateTime, toDateTime).stream()
                .map(this::convertToRunDto)
                .toList();
    }
}