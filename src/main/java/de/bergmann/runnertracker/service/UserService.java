package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.RunningTrackerUser;
import de.bergmann.runnertracker.repositories.UserRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    
    private UserRepo userRepo;
    @Getter
    private UserDetailsService userDetailsService;


    public RunningTrackerUser save(RunningTrackerUser userToSave) {
        if (userToSave.getId() == null) {
            userToSave.setCreatedAt(LocalDateTime.now());
        }
        userToSave.setUpdatedAt(LocalDateTime.now());
        return userRepo.save(userToSave);
    }

    public Optional<RunningTrackerUser> findUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }
}