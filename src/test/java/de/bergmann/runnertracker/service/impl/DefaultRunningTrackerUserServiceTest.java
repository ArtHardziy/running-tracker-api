package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultRunningTrackerUserServiceTest {

    @Mock
    RunningTrackerUserRepository userRepo;
    @Mock
    UserDetailsService userDetailsService;
    RunnerUser testRunnerUser;
    DefaultRunningTrackerUserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new DefaultRunningTrackerUserService(userRepo, userDetailsService);
        this.testRunnerUser = setUpTestRunnerWithRun();
    }

    @Test
    void save_whenUserToSaveGetIdNull() {
        testRunnerUser.setId(null);
        when(userRepo.save(any())).thenReturn(testRunnerUser);
        assertEquals(testRunnerUser, userService.save(testRunnerUser));
    }

    @Test
    void findAll() {
        when(userRepo.findAll()).thenReturn(List.of(testRunnerUser));
        assertEquals(List.of(testRunnerUser), userService.findAll());
    }

    @Test
    void findByIdShouldThrowResponseStatusException() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                () -> userService.findById(1L));
    }

    @Test
    void findById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testRunnerUser));
        assertEquals(testRunnerUser,userService.findById(1L));
    }

    @Test
    void findUserByUsername() {
        when(userRepo.findUserByUsername("test")).thenReturn(Optional.of(testRunnerUser));
        assertEquals(Optional.of(testRunnerUser),userService.findUserByUsername("test"));
    }

    @Test
    void countAllUsers() {
        when(userRepo.count()).thenReturn(2L);
        assertEquals(2L, userService.countAllUsers());
    }

    @Test
    void deleteById() {
        userService.deleteById(1L);
        verify(userRepo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void getUserDetailsService() {
        assertEquals(userDetailsService,userService.getUserDetailsService());
    }
}