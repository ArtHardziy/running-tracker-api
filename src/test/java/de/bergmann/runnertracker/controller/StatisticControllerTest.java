package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.TestUtils;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.StatisticDto;
import de.bergmann.runnertracker.service.AuthenticationService;
import de.bergmann.runnertracker.service.JwtService;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import de.bergmann.runnertracker.service.RunsService;
import de.bergmann.runnertracker.service.facade.StatisticFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static de.bergmann.runnertracker.TestUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = StatisticController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticFacade statisticFacade;
   @MockBean
   private JwtService jwtService;
   @MockBean
   private RunningTrackerUserService runningTrackerUserService;

   private static final String EXPECTED_GETUSERSTATISTICBYID_JSON = "{\"numberOfRuns\":1,\"totalDistance\":1000.0,\"averageSpeed\":10.0,\"runner\":{\"id\":1,\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"username\":\"test\",\"email\":\"test@test.com\",\"sex\":\"THEY\",\"birthdate\":\"01-01-2002\"},\"_links\":{\"users\":[{\"href\":\"http://localhost/v1/users/1\"},{\"href\":\"http://localhost/v1/users/1/runs{?from_date,to_date}\",\"templated\":true}]}}";

    private StatisticDto setUpStatisticDto() {
        var testRunnerWithRun = TestUtils.setUpTestRunnerWithRun();
        return StatisticDto.builder()
                .runner(TestUtils.convertToDto(testRunnerWithRun))
                .averageSpeed(10)
                .numberOfRuns(1)
                .totalDistance(1000)
                .build();
    }

    @Test
    void getUserStatisticById() throws Exception {
        var statisticDto = setUpStatisticDto();
        given(this.statisticFacade.getStatisticByUserIdAndBetweenDates(any(), any(), any()))
                .willReturn(statisticDto);
        this.mockMvc.perform(get("/v1/statistic/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_GETUSERSTATISTICBYID_JSON));
    }
}
