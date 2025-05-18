package be.pxl.projects.api.response;

import be.pxl.project.domain.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailDto(String name, LocalDateTime dueDate, ProjectStatus status, double totalCost, List<CostDto> costList) {
}
