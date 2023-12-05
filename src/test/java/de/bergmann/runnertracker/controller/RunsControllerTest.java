package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.model.dto.FinishRunDto;
import de.bergmann.runnertracker.model.dto.StartRunDto;
import de.bergmann.runnertracker.service.JwtService;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import de.bergmann.runnertracker.service.RunsService;
import de.bergmann.runnertracker.service.facade.RunnerUserFacade;
import de.bergmann.runnertracker.service.impl.RunnerUserModelAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static de.bergmann.runnertracker.TestUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = RunsController.class)
@AutoConfigureMockMvc(addFilters = false)
class RunsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RunsService runsService;
    @MockBean
    private RunnerUserFacade runnerUserFacade;
    @MockBean
    private RunnerUserModelAssembler runnerUserModelAssembler;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private RunningTrackerUserService runningTrackerUserService;

    private StartRunDto setUpStartRunDto() {
        var startRunDto = new StartRunDto();
        startRunDto.setStartDateTime(LocalDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.of(12,12)));
        startRunDto.setStartLatitude(53.901022);
        startRunDto.setStartLongitude(27.558596);
        startRunDto.setUserId(1L);
        return startRunDto;
    }

    private FinishRunDto setUpFinishRun() {
        var finishRunDto = new FinishRunDto();
        finishRunDto.setFinishDateTime(LocalDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.of(12,12)).plusSeconds(60));
        finishRunDto.setFinishLatitude(54.007194);
        finishRunDto.setFinishLongitude(27.871723);
        finishRunDto.setUserId(1L);
        return finishRunDto;
    }

    @Test
    void startRun() throws Exception {
        var startRun = setUpStartRunDto();
        this.mockMvc.perform(post("/v1/run/start")
                        .content(asJsonString(startRun))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void finishRun() throws Exception {
        var finishRun = setUpFinishRun();
        this.mockMvc.perform(post("/v1/run/finish")
                        .content(asJsonString(finishRun))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}