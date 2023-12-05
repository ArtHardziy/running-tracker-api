package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.TestUtils;
import de.bergmann.runnertracker.filters.JwtAuthenticationFilter;
import de.bergmann.runnertracker.model.dto.RunDto;
import de.bergmann.runnertracker.service.AuthenticationService;
import de.bergmann.runnertracker.service.facade.RunnerUserFacade;
import de.bergmann.runnertracker.service.facade.RunsFacade;
import de.bergmann.runnertracker.service.impl.RunnerUserModelAssembler;
import de.bergmann.runnertracker.service.impl.RunsModelAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserRunsRelationshipController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRunsRelationshipControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RunsFacade runsFacade;
    @MockBean
    private RunnerUserFacade userFacade;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtAuthenticationFilter authenticationFilter;
    @Autowired
    private RunsModelAssembler runsModelAssembler;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public RunsModelAssembler runnerUserModelAssembler() {
            return new RunsModelAssembler();
        }
    }

    @Test
    void getRunsByUserId() throws Exception {
        RunDto testRunDto = RunDto.builder()
                .id(1L)
                .userId(1L)
                .durationInSec(60)
                .finishDateTime(LocalDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.of(12, 12)).plusSeconds(60))
                .starDateTime(LocalDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.of(12, 12)))
                .distance(1000)
                .averageSpeed(16.6)
                .build();

        given(runsFacade.findRunsByUserIdBetweenDates(any(), any(), any()))
                .willReturn(List.of(testRunDto));

        this.mockMvc.perform(get("/v1/users/1/runs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"_embedded\":{\"runDtoList\":[{\"id\":1,\"userId\":1,\"distance\":1000.0,\"durationInSec\":60.0,\"averageSpeed\":16.6,\"starDateTime\":\"12-12-2023 12:12:00.000\",\"finishDateTime\":\"12-12-2023 12:13:00.000\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users/1\"},\"runs\":{\"href\":\"http://localhost/v1/users\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users\"}}}"));
    }
}