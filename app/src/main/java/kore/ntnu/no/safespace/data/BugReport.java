package kore.ntnu.no.safespace.data;

import java.io.Serializable;

/**
 * This class defines what a BugReport object looks like.
 *
 * @author Kristoffer
 */
public class BugReport  implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private final User user;

    public BugReport(String title, String description, User user) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public BugReport(Long id, String title, String description, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BugReport)) return false;

        BugReport bugReport = (BugReport) o;

        if (getTitle() != null ? !getTitle().equals(bugReport.getTitle()) : bugReport.getTitle() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(bugReport.getDescription()) : bugReport.getDescription() != null)
            return false;
        return getUser() != null ? getUser().equals(bugReport.getUser()) : bugReport.getUser() == null;
    }

    @Override
    public int hashCode() {
        int result = getTitle() != null ? getTitle().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
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

    public User getUser() {
        return user;
    }
}
