package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.controller.RunnerUserController;
import de.bergmann.runnertracker.model.dto.RunDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class RunsModelAssemblerTest {
    RunsModelAssembler runsModelAssembler = new RunsModelAssembler();
    RunDto runDto;

    @BeforeEach
    void setUp() {
        this.runDto = RunDto.builder()
                .id(1L)
                .finishDateTime(LocalDateTime.now())
                .starDateTime(LocalDateTime.now().minusSeconds(60))
                .averageSpeed(6)
                .distance(600)
                .durationInSec(60)
                .build();
    }

    @Test
    void toModel() {
        var expected = EntityModel.of(runDto,
                linkTo(methodOn(RunnerUserController.class).findOne(runDto.getId())).withSelfRel(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withRel("runs")
        );
        var actual = runsModelAssembler.toModel(runDto);
        assertEquals(expected, actual);
    }

    @Test
    void toCollectionModel() {
        var testRunnerUserList = List.of(runDto);

        var expected = CollectionModel.of(List.of(EntityModel.of(runDto,
                        linkTo(methodOn(RunnerUserController.class).findOne(runDto.getId())).withSelfRel(),
                        linkTo(methodOn(RunnerUserController.class).findAll()).withRel("runs"))
                ),
                linkTo(methodOn(RunnerUserController.class).findAll()).withSelfRel()
        );
        var actual = runsModelAssembler.toCollectionModel(testRunnerUserList);
        assertEquals(expected, actual);
    }
}