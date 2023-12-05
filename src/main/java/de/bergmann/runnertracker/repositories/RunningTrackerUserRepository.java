package de.bergmann.runnertracker.repositories;

import de.bergmann.runnertracker.model.RunnerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RunningTrackerUserRepository extends JpaRepository<RunnerUser, Long> {

    Optional<RunnerUser> findUserByUsername(String username);

    Optional<RunnerUser> findById(long id);

    Optional<RunnerUser> findUserByEmail(String email);
}