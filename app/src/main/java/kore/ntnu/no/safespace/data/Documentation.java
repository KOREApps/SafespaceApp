package kore.ntnu.no.safespace.data;

import java.io.Serializable;
import java.util.List;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class Documentation extends Report implements Serializable {
    private final User submitter;

    public Documentation(Long ID, String title, String description, List<Image> images, Project project, User submitter) {
        super(ID, title, description, images, project);
        this.submitter = submitter;
    }

    public Documentation(String title, String description, List<Image> images, Project project, User submitter) {
        this(null, title, description, images, project, submitter);
    }
    public Documentation(Long ID, String title, String description, User submitter) {
        this(ID, title, description, null, null, submitter);
    }

    public Documentation(String title, String description, List<Image> images, User submitter) {
        this(null, title, description, images, null, submitter);
    }

    public Documentation(String title, String description, Project project, User submitter) {
        this(null, title, description, null, project, submitter);
    }

    public Documentation(String title, String description, User submitter) {
        this(null, title, description, null, null, submitter);
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
