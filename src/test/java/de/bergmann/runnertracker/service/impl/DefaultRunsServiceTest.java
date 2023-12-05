package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.Run;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.FinishRunDto;
import de.bergmann.runnertracker.model.dto.StartRunDto;
import de.bergmann.runnertracker.repositories.RunsRepository;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultRunsServiceTest {

    @Mock
    RunsRepository runsRepository;
    @Mock
    RunningTrackerUserService runningTrackerUserService;
    RunnerUser testRunner;
    DefaultRunsService defaultRunsService;
    StartRunDto startRunDto;
    FinishRunDto finishRunDto;

    @BeforeEach
    void setUp() {
        this.testRunner = setUpTestRunnerWithRun();
        this.defaultRunsService = new DefaultRunsService(runsRepository, runningTrackerUserService);
        this.startRunDto = new StartRunDto();
        startRunDto.setUserId(1L);
        startRunDto.setStartLatitude(testRunner.getRuns().get(0).getStartLatitude());
        startRunDto.setStartDateTime(testRunner.getRuns().get(0).getStartDateTime());
        this.finishRunDto = new FinishRunDto();
        finishRunDto.setFinishLatitude(testRunner.getRuns().get(0).getFinishLatitude());
        finishRunDto.setUserId(testRunner.getId());
        finishRunDto.setFinishLongitude(testRunner.getRuns().get(0).getFinishLongitude());
        finishRunDto.setFinishDateTime(testRunner.getRuns().get(0).getFinishDateTime());
    }

    @Test
    void findAll() {
        when(runsRepository.findAll()).thenReturn(testRunner.getRuns());
        assertEquals(testRunner.getRuns(), defaultRunsService.findAll());
    }

    @Test
    void findRunsByUserIdShouldReturnEmptyList() {
        testRunner.setRuns(Collections.emptyList());
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertEquals(Collections.emptyList(), defaultRunsService.findRunsByUserId(1L));
    }

    @Test
    void findRunsByUserId() {
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertEquals(testRunner.getRuns(), defaultRunsService.findRunsByUserId(1L));
    }

    @Test
    void findRunsByUserIDBetweenDates_whenFromDateNull() {
        when(runsRepository.findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(1L, LocalDateTime.of(LocalDate.of(2000, 12, 12), LocalTime.MIN), LocalDateTime.of(LocalDate.of(2142, 12, 12), LocalTime.MAX))).thenReturn(testRunner.getRuns());
        var actual = defaultRunsService.findRunsByUserIdBetweenDates(1L, null, LocalDate.of(2142, 12, 12));
        assertEquals(testRunner.getRuns(), actual);
    }

    @Test
    void findRunsByUserIDBetweenDates_whenToDateNull() {
        when(runsRepository.findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(1L, LocalDateTime.of(LocalDate.of(2000, 12, 12), LocalTime.MIN), LocalDateTime.of(LocalDate.of(2142, 12, 12), LocalTime.MAX))).thenReturn(testRunner.getRuns());
        var actual = defaultRunsService.findRunsByUserIdBetweenDates(1L, LocalDate.of(2000, 12, 12), null);
        assertEquals(testRunner.getRuns(), actual);
    }

    @Test
    void findRunsByUserIDBetweenDates() {
        when(runsRepository.findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(1L, LocalDateTime.of(LocalDate.of(2000, 12, 12), LocalTime.MIN), LocalDateTime.of(LocalDate.of(2142, 12, 12), LocalTime.MAX))).thenReturn(testRunner.getRuns());
        var actual = defaultRunsService.findRunsByUserIdBetweenDates(1L, LocalDate.of(2000, 12, 12), LocalDate.of(2142, 12, 12));
        assertEquals(testRunner.getRuns(), actual);
    }

    @Test
    void findRunsByUserIDBetweenDates_shouldTrowResponseStatusExcpetion() {
        when(runsRepository.findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(1L, LocalDateTime.of(LocalDate.of(2000, 12, 12), LocalTime.MIN), LocalDateTime.of(LocalDate.of(2142, 12, 12), LocalTime.MAX)))
                .thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class,
                () -> defaultRunsService.findRunsByUserIdBetweenDates(1L, LocalDate.of(2000, 12, 12), LocalDate.of(2142, 12, 12)));
    }

    @Test
    void performStartRun_ShouldThrowResponseStatusException_whenRunnerHaseNotFinishedRuns() {
        testRunner.getRuns().get(0).setFinishDateTime(null);
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertThrows(ResponseStatusException.class,
                () -> defaultRunsService.performStartRun(this.startRunDto));
    }

    @Test
    void performStartRun() {
        when(runningTrackerUserService.findById(1L))
                .thenReturn(testRunner);
        defaultRunsService.performStartRun(startRunDto);
        verify(runningTrackerUserService).save(any(RunnerUser.class));
    }

    @Test
    void performFinishRun_shouldThrowResourceStatusException_whenHasNoRuns() {
        testRunner.setRuns(Collections.emptyList());
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertThrows(ResponseStatusException.class,
                () -> defaultRunsService.performFinishRun(finishRunDto));
    }

    @Test
    void performFinishRun_shouldThrowResourceStatusException_whenHasNoStartedRuns() {
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertThrows(ResponseStatusException.class,
                () -> defaultRunsService.performFinishRun(finishRunDto));
    }

    @Test
    void performFinishRun_shouldThrowResourceStatusException_whenHasInvalidFinishTime() {
        Run run = testRunner.getRuns().get(0);
        run.setFinishDateTime(null);
        finishRunDto.setFinishDateTime(LocalDateTime.of(LocalDate.of(2023, 12, 03), LocalTime.of(12, 12)).minusDays(1));
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        assertThrows(ResponseStatusException.class,
                () -> defaultRunsService.performFinishRun(finishRunDto));
    }

    @Test
    void performFinishRun() {
        Run run = testRunner.getRuns().get(0);
        run.setFinishDateTime(null);
        when(runningTrackerUserService.findById(1L)).thenReturn(testRunner);
        defaultRunsService.performFinishRun(finishRunDto);
        verify(runningTrackerUserService).save(any(RunnerUser.class));
    }


}