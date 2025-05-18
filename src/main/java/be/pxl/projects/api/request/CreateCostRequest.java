package be.pxl.projects.api.request;

import java.util.UUID;

public record CreateCostRequest(UUID projectId, double cost, String description) {
}
