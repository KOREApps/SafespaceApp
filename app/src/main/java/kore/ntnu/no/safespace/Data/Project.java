package kore.ntnu.no.safespace.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class Project implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private final List<User> contributors;

    public Project(Long id, String title, String description, List<User> contributors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contributors = new ArrayList<>();
        if(contributors != null){
            this.contributors.addAll(contributors);
        }
    }
    public Project(String title, String description) {
        this(null, title, description, null);
    }
    public Project (String title, String description, List<User> contributors){
        this(null, title, description, contributors);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (getId() != null ? !getId().equals(project.getId()) : project.getId() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(project.getTitle()) : project.getTitle() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(project.getDescription()) : project.getDescription() != null)
            return false;
        return getContributors() != null ? getContributors().equals(project.getContributors()) : project.getContributors() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getContributors() != null ? getContributors().hashCode() : 0);
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

    public List<User> getContributors() {
        return contributors;
    }

    public boolean isPartOf(User user){
        return contributors.contains(user);
    }
    public boolean addContributor(User user){
        return contributors.add(user);
    }
}
