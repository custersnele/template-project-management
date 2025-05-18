package be.pxl.projects.api.response;

import java.time.LocalDateTime;

public record ProjectDto(String name, LocalDateTime dueDate, double budget) {

}
