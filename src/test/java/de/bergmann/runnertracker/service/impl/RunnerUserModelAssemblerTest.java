package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.controller.RunnerUserController;
import de.bergmann.runnertracker.model.RunnerUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static de.bergmann.runnertracker.TestUtils.convertToDto;
import static de.bergmann.runnertracker.TestUtils.setUpTestRunnerWithRun;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class RunnerUserModelAssemblerTest {

    RunnerUser testRunnerUser;
    RunnerUserModelAssembler runnerUserModelAssembler;

    @BeforeEach
    void setUp() {
        this.testRunnerUser = setUpTestRunnerWithRun();
        this.runnerUserModelAssembler = new RunnerUserModelAssembler();
    }

    @Test
    void toModel() {
        var runnerUserDTO = convertToDto(testRunnerUser);
        var expected = EntityModel.of(runnerUserDTO,
                linkTo(methodOn(RunnerUserController.class).findOne(runnerUserDTO.getId())).withSelfRel(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withRel("users")
        );
        var actual = runnerUserModelAssembler.toModel(runnerUserDTO);
        assertEquals(expected, actual);
    }

    @Test
    void toCollectionModel() {
        var runnerUserDTO = convertToDto(testRunnerUser);
        var testRunnerUserList = List.of(runnerUserDTO);

        var expected = CollectionModel.of(List.of(EntityModel.of(runnerUserDTO,
                        linkTo(methodOn(RunnerUserController.class).findOne(runnerUserDTO.getId())).withSelfRel(),
                        linkTo(methodOn(RunnerUserController.class).findAll()).withRel("users"))
                ),
                linkTo(methodOn(RunnerUserController.class).findAll()).withSelfRel()
        );
        var actual = runnerUserModelAssembler.toCollectionModel(testRunnerUserList);
        assertEquals(expected, actual);
    }
}