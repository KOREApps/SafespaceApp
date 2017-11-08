package kore.ntnu.no.safespace.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class IncidentReport extends Report implements Serializable {
    private final List<String> keywords;

    public IncidentReport(Long id, String description, List<Image> images, List<String> keywords, Project project) {
        super(id,description,images,project);
        this.keywords = keywords;
    }

    public IncidentReport(String description, List<Image> images, List<String> keywords, Project project) {
        this(null, description, images, keywords, project);
    }

    public IncidentReport(String description, List<Image> images, Project project) {
        this(null, description, images, null, project);
    }

    public IncidentReport(String description, List<Image> images) {
        this(null, description, images, null, null);
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
