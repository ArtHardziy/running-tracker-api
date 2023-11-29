package de.bergmann.runningtracker.repositories;

import de.bergmann.runningtracker.model.RunningTrackerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<RunningTrackerUser, Long> {

    Optional<RunningTrackerUser> findUserByUsername(String username);

    Optional<RunningTrackerUser> findUserByEmail(String email);
}