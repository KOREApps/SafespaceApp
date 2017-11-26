package kore.ntnu.no.safespace.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines what a Project object looks like.
 *
 * @author Kristoffer
 */
public class Project implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final List<User> contributors;

    public Project(Long id, String name, String description, List<User> contributors) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.contributors = new ArrayList<>();
        if(contributors != null){
            this.contributors.addAll(contributors);
        }
    }
    public Project(String name, String description) {
        this(null, name, description, null);
    }
    public Project (String name, String description, List<User> contributors){
        this(null, name, description, contributors);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        if (getId() != null ? !getId().equals(project.getId()) : project.getId() != null)
            return false;
        if (getName() != null ? !getName().equals(project.getName()) : project.getName() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(project.getDescription()) : project.getDescription() != null)
            return false;
        return getContributors() != null ? getContributors().equals(project.getContributors()) : project.getContributors() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getContributors() != null ? getContributors().hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
