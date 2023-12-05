package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.controller.RunnerUserController;
import de.bergmann.runnertracker.model.dto.RunDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RunsModelAssembler implements RepresentationModelAssembler<RunDto, EntityModel<RunDto>> {
    @Override
    public EntityModel<RunDto> toModel(RunDto runModel) {
        return EntityModel.of(runModel,
                linkTo(methodOn(RunnerUserController.class).findOne(runModel.getId())).withSelfRel(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withRel("runs")
        );
    }

    public CollectionModel<EntityModel<RunDto>> toCollectionModel(List<RunDto> runList) {
        return CollectionModel.of(runList
                        .stream()
                        .map(this::toModel)
                        .toList(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withSelfRel()
        );
    }
}
