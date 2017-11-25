package kore.ntnu.no.safespace.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public abstract class Report implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private List<Image> images;
    private final Project project;

    public Report(Long id, String title, String description, List<Image> images, Project project) {
        this.id = id;
        this.title = title;
        this.description = description;
        if (images == null) {
            this.images = Collections.EMPTY_LIST;
        } else {
            this.images = images;
        }
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

    public String getTitle() {
        return title;
    }

    public List<Image> getImages() {
        return images;
    }

    public Project getProject() {
        return project;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
