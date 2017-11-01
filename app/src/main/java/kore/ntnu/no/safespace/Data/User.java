package kore.ntnu.no.safespace.Data;

import java.io.Serializable;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class User implements Serializable {
    private final Long uid;
    private final String username, firstname, surname;
    private final Role role;
    private final Long eid;

    public User(Long uid, String username, String firstName, String surName, Role role, Long eid) {
        this.uid = uid;
        this.username = username;
        this.firstname = firstName;
        this.surname = surName;
        this.role = role;
        this.eid = eid;
    }

    /**
     * Creates a new user
     * @param firstName the first name of the user
     * @param surName the second name of the user.
     * @param role the role of the user.
     */
    public User(String firstName, String surName, Role role) {
        this(null,null,firstName,surName,role,null);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getUid() != null ? !getUid().equals(user.getUid()) : user.getUid() != null)
            return false;
        if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null)
            return false;
        if (getFirstname() != null ? !getFirstname().equals(user.getFirstname()) : user.getFirstname() != null)
            return false;
        if (getSurName() != null ? !getSurName().equals(user.getSurName()) : user.getSurName() != null)
            return false;
        if (getRole() != null ? !getRole().equals(user.getRole()) : user.getRole() != null)
            return false;
        return getEid() != null ? getEid().equals(user.getEid()) : user.getEid() == null;
    }

    @Override
    public int hashCode() {
        int result = getUid() != null ? getUid().hashCode() : 0;
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getFirstname() != null ? getFirstname().hashCode() : 0);
        result = 31 * result + (getSurName() != null ? getSurName().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        result = 31 * result + (getEid() != null ? getEid().hashCode() : 0);
        return result;
    }

    public Long getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurName() {
        return surname;
    }

    public Role getRole() {
        return role;
    }

    public Long getEid() {
        return eid;
    }
}
