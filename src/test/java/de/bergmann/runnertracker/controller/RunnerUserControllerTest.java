package de.bergmann.runnertracker.controller;

import de.bergmann.runnertracker.TestUtils;
import de.bergmann.runnertracker.filters.JwtAuthenticationFilter;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import de.bergmann.runnertracker.service.AuthenticationService;
import de.bergmann.runnertracker.service.facade.RunnerUserFacade;
import de.bergmann.runnertracker.service.impl.RunnerUserModelAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static de.bergmann.runnertracker.TestUtils.asJsonString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = RunnerUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class RunnerUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RunnerUserFacade userFacade;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtAuthenticationFilter authenticationFilter;
    @Autowired
    private RunnerUserModelAssembler runnerUserModelAssembler;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public RunnerUserModelAssembler runnerUserModelAssembler() {
            return new RunnerUserModelAssembler();
        }
    }

    private final static String EXPECTED_FINDALL_JSON = "{\"_embedded\":{\"runnerUserDTOList\":[{\"id\":1,\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"username\":\"test\",\"email\":\"test@test.com\",\"sex\":\"THEY\",\"birthdate\":\"01-01-2002\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users/1\"},\"users\":{\"href\":\"http://localhost/v1/users\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users\"}}}";
    private final static String EXPECTED_FINDONE_JSON = "{\"id\":1,\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"username\":\"test\",\"email\":\"test@test.com\",\"sex\":\"THEY\",\"birthdate\":\"01-01-2002\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users/1\"},\"users\":{\"href\":\"http://localhost/v1/users\"}}}";
    private final static String EXPECTED_SAVE_JSON = "{\"id\":1,\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"username\":\"test\",\"email\":\"test@test.com\",\"sex\":\"THEY\",\"birthdate\":\"01-01-2002\",\"_links\":{\"self\":{\"href\":\"http://localhost/v1/users/1\"},\"users\":{\"href\":\"http://localhost/v1/users\"}}}";

    private List<RunnerUserDTO> setUpRunnerUserDto() {
        RunnerUser testRunnerWithRun = TestUtils.setUpTestRunnerWithRun();
        RunnerUserDTO runnerUserDTO = TestUtils.convertToDto(testRunnerWithRun);
        var runnerUsersDTOList = new ArrayList<RunnerUserDTO>();
        runnerUsersDTOList.add(runnerUserDTO);
        return runnerUsersDTOList;
    }

    @Test
    void findAll() throws Exception {
        given(this.userFacade.findAll())
                .willReturn(setUpRunnerUserDto());
        this.mockMvc.perform(get("/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_FINDALL_JSON));
    }

    @Test
    void findOne() throws Exception {
        given(this.userFacade.findById(1L))
                .willReturn(setUpRunnerUserDto().get(0));
        this.mockMvc.perform(get("/v1/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_FINDONE_JSON));
    }

    @Test
    void saveOne() throws Exception {
        var runnerUserDto = setUpRunnerUserDto().get(0);
        given(this.userFacade.save(runnerUserDto))
                .willReturn(runnerUserDto);
        this.mockMvc.perform(post("/v1/users")
                        .content(asJsonString(runnerUserDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_SAVE_JSON));
    }

    @Test
    void replaceOne() throws Exception {
        var runnerUserDto = setUpRunnerUserDto().get(0);
        given(this.userFacade.update(1L, runnerUserDto))
                .willReturn(runnerUserDto);
        this.mockMvc.perform(put("/v1/users/1")
                        .content(asJsonString(runnerUserDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_SAVE_JSON));
    }

    @Test
    void deleteOne() throws Exception {
        var runnerUserDto = setUpRunnerUserDto().get(0);
        given(this.userFacade.update(1L, runnerUserDto))
                .willReturn(runnerUserDto);
        this.mockMvc.perform(delete("/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}