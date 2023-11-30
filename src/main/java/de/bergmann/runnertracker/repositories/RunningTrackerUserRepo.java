package de.bergmann.runnertracker.repositories;

import de.bergmann.runnertracker.model.RunningTrackerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RunningTrackerUserRepo extends JpaRepository<RunningTrackerUser, Long> {

    Optional<RunningTrackerUser> findUserByUsername(String username);

    Optional<RunningTrackerUser> findById(long id);

    Optional<RunningTrackerUser> findUserByEmail(String email);
}