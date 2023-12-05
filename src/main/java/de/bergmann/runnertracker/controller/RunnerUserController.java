package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import de.bergmann.runnertracker.service.facade.RunnerUserFacade;
import de.bergmann.runnertracker.service.impl.RunnerUserModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class RunnerUserController {

    private final RunnerUserFacade userFacade;
    private final RunnerUserModelAssembler runnerUserModelAssembler;

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public CollectionModel<EntityModel<RunnerUserDTO>> findAll() {
        return runnerUserModelAssembler.toCollectionModel(userFacade.findAll());
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<RunnerUserDTO> findOne(@PathVariable Long id) {
        return runnerUserModelAssembler.toModel(userFacade.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<RunnerUserDTO> saveOne(@RequestBody RunnerUserDTO runnerUserDTO) {
        return runnerUserModelAssembler.toModel(userFacade.save(runnerUserDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<RunnerUserDTO> replaceOne(@RequestBody RunnerUserDTO runnerUserDTO, @PathVariable Long id) {
        return runnerUserModelAssembler.toModel(userFacade.update(id, runnerUserDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public void deleteOne(@PathVariable Long id) {
        userFacade.deleteById(id);
    }
}