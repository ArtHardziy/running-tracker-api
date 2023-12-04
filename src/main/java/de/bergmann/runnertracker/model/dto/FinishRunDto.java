package de.bergmann.runnertracker.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FinishRunDto {
    @JsonAlias("runner_id")
    private long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonAlias("finish_datetime")
    private LocalDateTime finishDateTime;
    @JsonAlias("finish_latitude")
    private double finishLatitude;
    @JsonAlias("finish_longitude")
    private double finishLongitude;
}
