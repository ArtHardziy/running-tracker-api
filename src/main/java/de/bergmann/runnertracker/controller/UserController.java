package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.model.dto.RunningTrackerUserDTO;
import de.bergmann.runnertracker.model.facade.RunningTrackerUserFacade;
import de.bergmann.runnertracker.service.RunningTrackerUserModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private RunningTrackerUserFacade userFacade;
    private RunningTrackerUserModelAssembler runningTrackerUserModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<RunningTrackerUserDTO>> findAll() {
        return CollectionModel.of(
                userFacade.findAll().stream()
                        .map(runningTrackerUserModelAssembler::toModel)
                        .toList(),
                linkTo(methodOn(UserController.class).findAll()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<RunningTrackerUserDTO> findOne(@PathVariable Long id) {
        return runningTrackerUserModelAssembler.toModel(userFacade.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE')")
    public EntityModel<RunningTrackerUserDTO> saveOne(@RequestBody RunningTrackerUserDTO runningTrackerUserDTO) {
        return runningTrackerUserModelAssembler.toModel(userFacade.save(runningTrackerUserDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public EntityModel<RunningTrackerUserDTO> replaceOne(@RequestBody RunningTrackerUserDTO runningTrackerUserDTO, @PathVariable Long id) {
        return runningTrackerUserModelAssembler.toModel(userFacade.update(id, runningTrackerUserDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteOne(@PathVariable Long id) {
        userFacade.deleteById(id);
    }
}