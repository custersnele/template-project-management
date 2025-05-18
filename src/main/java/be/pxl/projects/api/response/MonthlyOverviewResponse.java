package be.pxl.projects.api.response;

import be.pxl.project.domain.ProjectStatus;

import java.util.List;
import java.util.Map;

public record MonthlyOverviewResponse(double totalBudget, double budgetApproved, Map<ProjectStatus, List<ProjectDto>> projects) {
}
