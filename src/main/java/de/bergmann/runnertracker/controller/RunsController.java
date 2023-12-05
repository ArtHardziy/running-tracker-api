package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.model.dto.FinishRunDto;
import de.bergmann.runnertracker.model.dto.StartRunDto;
import de.bergmann.runnertracker.service.RunsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/run")
@AllArgsConstructor
public class RunsController {

    private RunsService runsService;

    @PostMapping("/start")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<?> startRun(@RequestBody StartRunDto startRunDto) {
        runsService.performStartRun(startRunDto);
        return EntityModel.of(ResponseEntity.ok().build());
    }

    @PostMapping("/finish")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public EntityModel<?> finishRun(@RequestBody FinishRunDto finishRunDto) {
        runsService.performFinishRun(finishRunDto);
        return EntityModel.of(ResponseEntity.ok().build());
    }
}