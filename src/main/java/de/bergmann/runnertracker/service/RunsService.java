package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.Run;
import de.bergmann.runnertracker.model.dto.FinishRunDto;
import de.bergmann.runnertracker.model.dto.StartRunDto;

import java.time.LocalDate;
import java.util.List;

public interface RunsService {

    List<Run> findAll();

    List<Run> findRunsByUserId(Long userId);

    void performStartRun(StartRunDto startRunDto);

    void performFinishRun(FinishRunDto finishRunDto);

    List<Run> findRunsByUserIdBetweenDates(long userId, LocalDate fromDate, LocalDate toDate);
}
