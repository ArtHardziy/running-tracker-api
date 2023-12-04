package de.bergmann.runnertracker.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.service.facade.RunsFacade;
import de.bergmann.runnertracker.service.impl.RunsModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class UserRunsRelationshipController {

    private RunsFacade runsFacade;
    private RunsModelAssembler runsModelAssembler;

    @GetMapping("/users/{id}/runs")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public CollectionModel<EntityModel<RunDto>> getRunsByUserId(
            @PathVariable Long id,
            @RequestParam(value = "from_date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate fromDateTime,
            @RequestParam(value = "to_date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate toDateTime
    ) {
        return runsModelAssembler.toCollectionModel(runsFacade.findRunsByUserIdBetweenDates(id, fromDateTime, toDateTime));
    }
}
