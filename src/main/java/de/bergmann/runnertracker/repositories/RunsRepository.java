package de.bergmann.runnertracker.repositories;

import de.bergmann.runnertracker.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RunsRepository extends JpaRepository<Run, Long> {

    Optional<Run> findByRunningUserId(Long id);

    List<Run> findByRunningUserIdAndStartDateTimeGreaterThanEqualAndFinishDateTimeLessThanEqual(long id, LocalDateTime fromDateTime, LocalDateTime toDateTime);

}
