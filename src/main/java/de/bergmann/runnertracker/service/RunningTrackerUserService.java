package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.RunnerUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface RunningTrackerUserService {
    RunnerUser save(RunnerUser userToSave);

    List<RunnerUser> findAll();

    RunnerUser findById(long id);

    Optional<RunnerUser> findUserByUsername(String username);

    long countAllUsers();

    void deleteById(Long id);

    UserDetailsService getUserDetailsService();
}
