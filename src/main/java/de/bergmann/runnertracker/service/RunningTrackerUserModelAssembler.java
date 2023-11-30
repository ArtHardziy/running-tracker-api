package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.controller.UserController;
import de.bergmann.runnertracker.model.dto.RunningTrackerUserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RunningTrackerUserModelAssembler implements RepresentationModelAssembler<RunningTrackerUserDTO, EntityModel<RunningTrackerUserDTO>> {
    @Override
    public EntityModel<RunningTrackerUserDTO> toModel(RunningTrackerUserDTO runningTrackerUserDTO) {
        return EntityModel.of(runningTrackerUserDTO,
                linkTo(methodOn(UserController.class ).findOne(runningTrackerUserDTO.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).findAll()).withRel("users")
        );
    }
}
