package kore.ntnu.no.safespace.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class Documentation extends Report implements Serializable{
    private final User submitter;

    public Documentation(Long ID, String description, List<Image> images, Project project, User submitter) {
        super(ID, description, images, project);
        this.submitter = submitter;
    }
    public Documentation(String description, List<Image> images, Project project, User submitter) {
        super(null, description, images, project);
        this.submitter = submitter;
    }
    public Documentation(String description, List<Image> images, User submitter) {
        super(null, description, images, null);
        this.submitter = submitter;
    }
    public Documentation(String description, Project project, User submitter) {
        super(null, description, null, project);
        this.submitter = submitter;
    }
    public Documentation(String description, User submitter) {
        super(null, description, null, null);
        this.submitter = submitter;
    }

    public User getSubmitter() {
        return submitter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Documentation)) return false;
        if (!super.equals(o)) return false;

        Documentation that = (Documentation) o;

        return getSubmitter() != null ? getSubmitter().equals(that.getSubmitter()) : that.getSubmitter() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSubmitter() != null ? getSubmitter().hashCode() : 0);
        return result;
    }
}
