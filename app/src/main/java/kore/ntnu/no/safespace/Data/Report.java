package kore.ntnu.no.safespace.Data;

import java.util.List;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public abstract class Report {
    private final Long id;
    private final String description;
    private final List<Image> images;
    private final Project project;

    public Report(Long id, String description, List<Image> images, Project project) {
        this.id = id;
        this.description = description;
        this.images = images;
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;

        Report report = (Report) o;

        if (getId() != null ? !getId().equals(report.getId()) : report.getId() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(report.getDescription()) : report.getDescription() != null)
            return false;
        if (getImages() != null ? !getImages().equals(report.getImages()) : report.getImages() != null)
            return false;
        return getProject() != null ? getProject().equals(report.getProject()) : report.getProject() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getImages() != null ? getImages().hashCode() : 0);
        result = 31 * result + (getProject() != null ? getProject().hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<Image> getImages() {
        return images;
    }

    public Project getProject() {
        return project;
    }
}
