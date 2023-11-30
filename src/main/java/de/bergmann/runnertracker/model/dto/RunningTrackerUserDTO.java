package de.bergmann.runnertracker.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.bergmann.runnertracker.model.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunningTrackerUserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Sex sex;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
}
