package de.bergmann.runnertracker.model.facade;

import de.bergmann.runnertracker.model.RunningTrackerUser;
import de.bergmann.runnertracker.model.Sex;
import de.bergmann.runnertracker.model.dto.RunningTrackerUserDTO;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RunningTrackerUserFacade {

    private RunningTrackerUserService userService;

    public List<RunningTrackerUserDTO> findAll() {
        final List<RunningTrackerUser> allUsers = userService.findAll();
        if (allUsers.isEmpty()) {
            return Collections.emptyList();
        }
        return allUsers.stream()
                .map(this::convertToDto)
                .toList();
    }

    public RunningTrackerUserDTO findById(long id) {
        return this.convertToDto(userService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Running user with id %s not found", id))
        ));
    }

    protected RunningTrackerUserDTO convertToDto(RunningTrackerUser user) {
        return RunningTrackerUserDTO.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthdate(user.getBirthDate())
                .build();
    }

    public RunningTrackerUserDTO save(RunningTrackerUserDTO userDtoToSave) {
        if (userService.findUserByUsername(userDtoToSave.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exist, cannot save");
        }
        var userModelToSave = RunningTrackerUser.builder()
                .id(userDtoToSave.getId())
                .birthDate(userDtoToSave.getBirthdate())
                .lastName(userDtoToSave.getLastName())
                .firstName(userDtoToSave.getFirstName())
                .sex(userDtoToSave.getSex())
                .username(userDtoToSave.getUsername())
                .email(userDtoToSave.getEmail())
                .build();
        RunningTrackerUser savedUserModel = userService.save(userModelToSave);
        return convertToDto(savedUserModel);
    }

    public RunningTrackerUserDTO update(Long id, RunningTrackerUserDTO userWithUpdates) {
        RunningTrackerUser modelToUpdateById = userService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id [%s] not found", id))
        );
        updateRunningTrackerUserModel(modelToUpdateById, userWithUpdates);
        RunningTrackerUser savedUserModel = userService.save(modelToUpdateById);
        return convertToDto(savedUserModel);
    }

    private void updateRunningTrackerUserModel(RunningTrackerUser modelToUpdate, RunningTrackerUserDTO dtoWithUpdates) {
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