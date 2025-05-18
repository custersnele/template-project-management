package be.pxl.projects.api.request;

import be.pxl.project.domain.ProjectPriority;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CreateProjectRequest(String name, String description, @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") LocalDateTime dueDate, double budget, ProjectPriority priority) {
}
