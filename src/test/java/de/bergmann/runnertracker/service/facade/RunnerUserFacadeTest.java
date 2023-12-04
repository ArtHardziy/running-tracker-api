package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunnerUserFacadeTest {

    @Mock
    RunningTrackerUserService userService;
    RunnerUser testRunner;
    RunnerUserFacade  runnerUserFacade;
    RunnerUserDTO testRunnerUserDto;

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
        this.runnerUserFacade = new RunnerUserFacade(userService);
    }

    @Test
    void findAllShouldReturnEmptyList() {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        var expected = Collections.emptyList();
        var actual = this.runnerUserFacade.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void findAllShouldReturnListWithOneUserDto() {
        when(userService.findAll()).thenReturn(List.of(testRunner));
        var expected = List.of(this.testRunnerUserDto);
        var actual = runnerUserFacade.findAll();
        assertEquals(1, expected.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    void findById() {
        when(userService.findById(1L)).thenReturn(testRunner);
        var actual = runnerUserFacade.findById(1L);
        assertEquals(this.testRunnerUserDto, actual);
    }

    @Test
    void saveExpectedThrowResponseStatusException() {
        when(userService.findUserByUsername("test")).thenReturn(Optional.of(this.testRunner));
        Assertions.assertThrows(ResponseStatusException.class,
                () -> runnerUserFacade.save(this.testRunnerUserDto));
    }

    @Test
    void saveShouldReturnExpectedRunnerUserDto() {
        when(userService.findUserByUsername("test")).thenReturn(Optional.empty());
        when(userService.save(any())).thenReturn(testRunner);
        var expected = this.testRunnerUserDto;
        var actual = runnerUserFacade.save(testRunnerUserDto);
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldReturnExpectedRunnerUserDto() {
        when(userService.findById(1L)).thenReturn(this.testRunner);
        when(userService.save(any())).thenReturn(this.testRunner);
        var expected = this.testRunnerUserDto;
        var actual = runnerUserFacade.update(1L, this.testRunnerUserDto);
        assertEquals(expected, actual);
    }

    @Test
    void deleteByIdMethodWasCalledOnce() {
        runnerUserFacade.deleteById(1L);
        verify(userService, Mockito.times(1)).deleteById(1L);
    }
}