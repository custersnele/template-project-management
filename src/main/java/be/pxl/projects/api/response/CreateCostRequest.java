package be.pxl.projects.api.response;

import java.util.UUID;

public record CreateCostRequest (UUID projectId, double amount, String description) {
}
