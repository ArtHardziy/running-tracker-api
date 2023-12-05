package de.bergmann.runnertracker.service.facade;

import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.model.Sex;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RunnerUserFacade {

    private RunningTrackerUserService userService;

    public List<RunnerUserDTO> findAll() {
        final List<RunnerUser> allUsers = userService.findAll();
        if (allUsers.isEmpty()) {
            return Collections.emptyList();
        }
        return allUsers.stream()
                .map(this::convertToDto)
                .toList();
    }

    public RunnerUserDTO findById(long id) {
        return this.convertToDto(userService.findById(id));
    }

    protected RunnerUserDTO convertToDto(RunnerUser user) {
        return RunnerUserDTO.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthdate(user.getBirthDate())
                .sex(user.getSex())
                .build();
    }

    public RunnerUserDTO save(RunnerUserDTO runnerUserDTOToSave) {
        if (userService.findUserByUsername(runnerUserDTOToSave.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist, cannot save");
        }
        var userModelToSave = RunnerUser.builder()
                .id(runnerUserDTOToSave.getId())
                .birthDate(runnerUserDTOToSave.getBirthdate())
                .lastName(runnerUserDTOToSave.getLastName())
                .firstName(runnerUserDTOToSave.getFirstName())
                .sex(runnerUserDTOToSave.getSex())
                .username(runnerUserDTOToSave.getUsername())
                .email(runnerUserDTOToSave.getEmail())
                .build();
        RunnerUser savedUserModel = userService.save(userModelToSave);
        return convertToDto(savedUserModel);
    }

    public RunnerUserDTO update(Long id, RunnerUserDTO userWithUpdates) {
        RunnerUser modelToUpdateById = userService.findById(id);
        updateRunningTrackerUserModel(modelToUpdateById, userWithUpdates);
        RunnerUser savedUserModel = userService.save(modelToUpdateById);
        return convertToDto(savedUserModel);
    }

    private void updateRunningTrackerUserModel(RunnerUser modelToUpdate, RunnerUserDTO dtoWithUpdates) {
        LocalDate birthdateToUpdate = dtoWithUpdates.getBirthdate();
        String emailToUpdate = dtoWithUpdates.getEmail();
        Sex sexToUpdate = dtoWithUpdates.getSex();
        String firstNameToUpdate = dtoWithUpdates.getFirstName();
        String lastNameToUpdate = dtoWithUpdates.getLastName();

        if (birthdateToUpdate != null) {
            modelToUpdate.setBirthDate(birthdateToUpdate);
        }
        if (emailToUpdate != null) {
            modelToUpdate.setEmail(emailToUpdate);
        }
        if (sexToUpdate != null) {
            modelToUpdate.setSex(sexToUpdate);
        }
        if (firstNameToUpdate != null) {
            modelToUpdate.setFirstName(firstNameToUpdate);
        }
        if (lastNameToUpdate != null) {
            modelToUpdate.setLastName(lastNameToUpdate);
        }
    }

    public void deleteById(Long id) {
        userService.deleteById(id);
    }
}