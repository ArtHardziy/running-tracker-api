package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.controller.RunnerUserController;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RunnerUserModelAssembler implements RepresentationModelAssembler<RunnerUserDTO, EntityModel<RunnerUserDTO>> {
    @Override
    public EntityModel<RunnerUserDTO> toModel(RunnerUserDTO runnerUserDTO) {
        return EntityModel.of(runnerUserDTO,
                linkTo(methodOn(RunnerUserController.class).findOne(runnerUserDTO.getId())).withSelfRel(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withRel("users")
        );
    }


    public CollectionModel<EntityModel<RunnerUserDTO>> toCollectionModel(List<RunnerUserDTO> userList) {
        return CollectionModel.of(
                userList.stream()
                        .map(this::toModel)
                        .toList(),
                linkTo(methodOn(RunnerUserController.class).findAll()).withSelfRel()
        );
    }
}
