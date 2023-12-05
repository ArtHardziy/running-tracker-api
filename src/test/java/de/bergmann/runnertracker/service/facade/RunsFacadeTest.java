package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.service.RunsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunsFacadeTest {

    @Mock
    RunsService runsService;
    RunsFacade runsFacade;

    @BeforeEach
    public void setUp() {
        var testRunner = setUpTestRunnerWithRun();
        when(runsService.findRunsByUserIdBetweenDates(1L, LocalDate.MIN, LocalDate.MAX))
                .thenReturn(List.of(testRunner.getRuns().get(0)));
        this.runsFacade = new RunsFacade(runsService);
    }

    @Test
    void findRunsByUserIdBetweenDates() {
        List<RunDto> actualRuns = runsFacade.findRunsByUserIdBetweenDates(1L, LocalDate.MIN, LocalDate.MAX);
        assertFalse(actualRuns.isEmpty());
        var actualRunDto = actualRuns.get(0);
        final double actualDistance = actualRunDto.getDistance();
        final double actualAverageSpeed = actualRunDto.getAverageSpeed();
        final double actualDurationInSec = actualRunDto.getDurationInSec();

        final double expectedDistance = 981.745604311616;
        final double expectedDurationInSec = 370.0;
        final double expectedAverageSpeed = 2.6533664981395026;

        assertEquals(expectedDistance, actualDistance);
        assertEquals(expectedDurationInSec, actualDurationInSec);
        assertEquals(expectedAverageSpeed, actualAverageSpeed);
    }
}