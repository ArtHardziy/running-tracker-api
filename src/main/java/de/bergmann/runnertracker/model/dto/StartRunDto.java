package de.bergmann.runnertracker.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StartRunDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonAlias("runner_id")
    private Long userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Berlin")
    @JsonAlias("start_datetime")
    private LocalDateTime startDateTime;
    @JsonAlias("start_latitude")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double startLatitude;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonAlias("start_longitude")
    private double startLongitude;
}
