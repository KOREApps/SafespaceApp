package kore.ntnu.no.safespace.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * This class defines what an IncidentReport object looks like.
 *
 * @author Kristoffer
 */
public class IncidentReport extends Report implements Serializable {
    private final List<String> keywords;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public IncidentReport(Long id, String title, String description, List<Image> images, List<String> keywords, Project project) {
        super(id,title,description,images,project);
        this.keywords = keywords;
    }

    public IncidentReport(String title, String description, List<Image> images, List<String> keywords, Project project) {
        this(null, title, description, images, keywords, project);
    }

    public IncidentReport(String title, String description, List<Image> images, Project project) {
        this(null, title, description, images, null, project);
    }

    public IncidentReport(String title, String description, List<Image> images) {
        this(null, title, description, images, null, null);
    }

    public IncidentReport(Long id, String title, String description) {
        this(id, title, description, Collections.emptyList(), Collections.emptyList(), null);
    }

    public IncidentReport(String description) {
        this(null, description, null, null, null);
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncidentReport)) return false;
        if (!super.equals(o)) return false;

        IncidentReport that = (IncidentReport) o;

        return getKeywords() != null ? getKeywords().equals(that.getKeywords()) : that.getKeywords() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getKeywords() != null ? getKeywords().hashCode() : 0);
        return result;
    }
}
