package be.pxl.projects.service;


import be.pxl.projects.domain.Project;
import be.pxl.projects.exception.InvalidProjectException;

public interface ValidationRule {
    void validate(Project project) throws InvalidProjectException;
}
