package kore.ntnu.no.safespace.dto;

import java.security.Policy;

import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.Project;

/**
 * Created by Robert on 11-Nov-17.
 */

public class ReportDTO {

    private Long id;
    private String title;
    private String description;
    private Project project;

    public ReportDTO(Long id, String title, String description, Project project) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.project = project;
    }

    public ReportDTO(IncidentReport report) {
        this.id = report.getId();
        this.title = report.getTitle();
        this.description = report.getDescription();
        this.project = report.getProject();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
