package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.Run;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import de.bergmann.runnertracker.model.dto.StatisticDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticFacadeTest {
    @Mock
    RunnerUserFacade userFacade;
    @Mock
    RunsFacade runsFacade;
    RunnerUser testRunner;
    RunnerUserDTO testRunnerUserDto;
    StatisticFacade statisticFacade;
    RunDto testRunDto;

    @BeforeEach
    public void setUp() {
        this.testRunner = setUpTestRunnerWithRun();
        this.testRunnerUserDto = RunnerUserDTO.builder()
                .id(testRunner.getId())
                .email(testRunner.getEmail())
                .birthdate(testRunner.getBirthDate())
                .firstName(testRunner.getFirstName())
                .lastName(testRunner.getLastName())
                .username(testRunner.getUsername())
                .sex(testRunner.getSex())
                .build();
        var testRun = testRunner.getRuns().get(0);
        this.testRunDto = RunDto.builder()
                .id(testRun.getId())
                .userId(testRun.getRunningUserId())
                .distance(1000)
                .durationInSec(360)
                .averageSpeed((double) 1000 / 360)
                .starDateTime(testRun.getStartDateTime())
                .finishDateTime(testRun.getFinishDateTime())
                .build();
        this.statisticFacade = new StatisticFacade(userFacade,runsFacade);
    }

    @Test
    public void getStatisticByUserIdAndBetweenDates() {
        when(runsFacade.findRunsByUserIdBetweenDates(1L, LocalDate.MIN, LocalDate.MAX))
                .thenReturn(List.of(testRunDto));
        when(userFacade.findById(1L)).thenReturn(testRunnerUserDto);
        var expected = StatisticDto.builder()
                .runner(this.testRunnerUserDto)
                .totalDistance(1000)
                .numberOfRuns(1)
                .averageSpeed((double) 1000 /360)
                .runner(testRunnerUserDto)
                .build();
        var actual = statisticFacade.getStatisticByUserIdAndBetweenDates(1L, LocalDate.MIN, LocalDate.MAX);
        assertEquals(expected, actual);
    }
}