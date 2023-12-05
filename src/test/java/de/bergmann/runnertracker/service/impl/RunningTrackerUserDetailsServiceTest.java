package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.TestUtils;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.RunningTrackerUserPrincipal;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunningTrackerUserDetailsServiceTest {

    @Mock
    RunningTrackerUserRepository runningTrackerUserRepository;
    RunningTrackerUserDetailsService runningTrackerUserDetailsService;
    RunnerUser runnerUser;

    @BeforeEach
    void setup() {
        this.runnerUser = TestUtils.setUpTestRunnerWithRun();
        this.runningTrackerUserDetailsService = new RunningTrackerUserDetailsService(runningTrackerUserRepository);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenRunnerUserDoesntExist() {
        when(runningTrackerUserRepository.findUserByUsername("test")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> runningTrackerUserDetailsService.loadUserByUsername("test"));
    }

    @Test
    void loadUserByUsername() {
        when(runningTrackerUserRepository.findUserByUsername("test")).thenReturn(Optional.of(runnerUser));
        var actual = runningTrackerUserDetailsService.loadUserByUsername("test");
        var expected = RunningTrackerUserPrincipal.create(runnerUser);
        assertEquals(expected,actual);
    }
}