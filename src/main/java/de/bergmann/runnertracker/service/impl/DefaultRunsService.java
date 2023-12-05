package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.Run;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.FinishRunDto;
import de.bergmann.runnertracker.model.dto.StartRunDto;
import de.bergmann.runnertracker.repositories.RunsRepository;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import de.bergmann.runnertracker.service.RunsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DefaultRunsService implements RunsService {

    private RunsRepository runsRepo;
    private RunningTrackerUserService userService;

    @Override
    public List<Run> findAll() {
        return runsRepo.findAll();
    }

    @Override
    public List<Run> findRunsByUserId(final Long userId) {
        var runner = userService.findById(userId);
        var runs = runner.getRuns();
        if (runs.isEmpty()) {
            return Collections.emptyList();
        }
        return runs;
    }

    @Override
    public void performStartRun(StartRunDto startRunDto) {
        var runner = userService.findById(startRunDto.getUserId());
        if (isRunnerHasNotFinishedRuns(runner)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("User with id [%s] has already started runs. Pls finish existing run.", startRunDto.getUserId())
            );
        }
        var startedRun = Run.builder()
                .runningUserId(startRunDto.getUserId())
                .startDateTime(startRunDto.getStartDateTime())
                .startLatitude(startRunDto.getStartLatitude())
                .startLongitude(startRunDto.getStartLongitude())
                .build();
        runner.getRuns().add(startedRun);
        userService.save(runner);
    }

    private boolean isRunnerHasNotFinishedRuns(RunnerUser runner) {
        var notFinishedRuns = runner.getRuns().stream()
                .filter(run -> Objects.isNull(run.getFinishDateTime()))
                .toList();
        return !notFinishedRuns.isEmpty();
    }

    @Override
    public void performFinishRun(FinishRunDto finishRunDto) {
        var runner = userService.findById(finishRunDto.getUserId());

        var notFinishedRun = runner.getRuns().stream()
                .filter(run -> Objects.isNull(run.getFinishDateTime()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        String.format("User with id [%s] has no runs started. Pls start run.", finishRunDto.getUserId()))
                );
        if (notFinishedRun.getStartDateTime().isAfter(finishRunDto.getFinishDateTime())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Invalid Finished time. Finish time cannot be before startTime. Your finished time [%s], while start time [%s]", notFinishedRun.getStartDateTime(), finishRunDto.getFinishDateTime())
            );
        }
        notFinishedRun.setFinishLongitude(finishRunDto.getFinishLongitude());
        notFinishedRun.setFinishLatitude(finishRunDto.getFinishLatitude());
        notFinishedRun.setFinishDateTime(finishRunDto.getFinishDateTime());

        userService.save(runner);
    }

    @Override
    public List<Run> findRunsByUserIdBetweenDates(long userId, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            fromDate = LocalDate.of(2000, 12, 12);
        }
        if (toDate == null) {
            toDate = LocalDate.of(2142, 12, 12);
        }
        var runsBetweenTimes = runsRepo.findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(userId, LocalDateTime.of(fromDate, LocalTime.MIN), LocalDateTime.of(toDate, LocalTime.MAX));
        if (runsBetweenTimes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Runs associated with runner id [%s] between [%s] fromDate and [%s] toDate not found", userId, fromDate, toDate));
        }
        return runsBetweenTimes;
    }
}
