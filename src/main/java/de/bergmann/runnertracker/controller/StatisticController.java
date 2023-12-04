package de.bergmann.runnertracker.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bergmann.runnertracker.model.dto.StatisticDto;
import de.bergmann.runnertracker.service.facade.StatisticFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/statistic")
@AllArgsConstructor
public class StatisticController {

    private StatisticFacade statisticFacade;

    @GetMapping("/{userId}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<StatisticDto> getUserStatisticById(
            @PathVariable Long userId,
            @RequestParam(value = "from_date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate fromDateTime,
            @RequestParam(value = "to_date", required = false) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate toDateTime
    ) {
        return EntityModel.of(
                statisticFacade.getStatisticByUserIdAndBetweenDates(userId, fromDateTime, toDateTime),
                linkTo(methodOn(RunnerUserController.class).findOne(userId)).withRel("users"),
                linkTo(methodOn(UserRunsRelationshipController.class).getRunsByUserId(userId, fromDateTime, toDateTime)).withRel("users")
        );
    }
}