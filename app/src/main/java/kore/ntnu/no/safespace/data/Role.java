package kore.ntnu.no.safespace.data;

import java.io.Serializable;

/**
 * This class defines what a Role object looks like.
 *
 * @author Kristoffer
 */
public class Role implements Serializable {
    private final Long id;
    private final String rolename;

    public Role(Long id, String roleName) {
        this.id = id;
        this.rolename = roleName;
    }

    public Role(String roleName) {
        this((long) -1,roleName);
    }

    public Long getId() {
        return id;
    }
    public String getRoleName() {
        return rolename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        if (!getId().equals(role.getId())) return false;
        return getRoleName().equals(role.getRoleName());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getRoleName().hashCode();
        return result;
    }
}
