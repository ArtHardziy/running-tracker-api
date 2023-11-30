package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.RunningTrackerUser;
import de.bergmann.runnertracker.model.dto.RunningTrackerUserDTO;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RunningTrackerUserService {
    
    private RunningTrackerUserRepo runningTrackerUserRepo;
    @Getter
    private UserDetailsService userDetailsService;


    public RunningTrackerUser save(RunningTrackerUser userToSave) {
        if (userToSave.getId() == null) {
            userToSave.setCreatedAt(LocalDateTime.now());
        }
        userToSave.setUpdatedAt(LocalDateTime.now());
        return runningTrackerUserRepo.save(userToSave);
    }

    public List<RunningTrackerUser> findAll() {
        return runningTrackerUserRepo.findAll();
    }

    public Optional<RunningTrackerUser> findById(final long id) {
        return runningTrackerUserRepo.findById(id);
    }

    public Optional<RunningTrackerUser> findUserByUsername(String username) {
        return runningTrackerUserRepo.findUserByUsername(username);
    }

    public long countAllUsers() {
        return runningTrackerUserRepo.count();
    }

    public void deleteById(Long id) {
        runningTrackerUserRepo.deleteById(id);
    }
}