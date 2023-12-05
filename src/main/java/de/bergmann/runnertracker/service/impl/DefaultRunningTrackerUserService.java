package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepository;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultRunningTrackerUserService implements RunningTrackerUserService {

    private RunningTrackerUserRepository runningTrackerUserRepo;
    private UserDetailsService userDetailsService;

    @Override
    public RunnerUser save(RunnerUser userToSave) {
        if (userToSave.getId() == null) {
            userToSave.setCreatedAt(LocalDateTime.now());
        }
        userToSave.setUpdatedAt(LocalDateTime.now());
        return runningTrackerUserRepo.save(userToSave);
    }

    @Override
    public List<RunnerUser> findAll() {
        return runningTrackerUserRepo.findAll();
    }

    @Override
    public RunnerUser findById(final long id) {
        return runningTrackerUserRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id [%s] not found", id))
        );
    }

    @Override
    public Optional<RunnerUser> findUserByUsername(String username) {
        return runningTrackerUserRepo.findUserByUsername(username);
    }

    @Override
    public long countAllUsers() {
        return runningTrackerUserRepo.count();
    }

    @Override
    public void deleteById(Long id) {
        runningTrackerUserRepo.deleteById(id);
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }
}