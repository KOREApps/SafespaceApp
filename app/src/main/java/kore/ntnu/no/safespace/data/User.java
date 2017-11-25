package kore.ntnu.no.safespace.data;

import java.io.Serializable;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class  User implements Serializable {
    private final Long id;
    private final String username, firstName, lastName;
    private String password;
    private final Role role;
    private final Long eid;

    public User(Long id, String username, String firstName, String surName,String password, Role role, Long eid) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = surName;
        this.password = password;
        this.role = role;
        this.eid = eid;
    }

    /**
     * Creates a new user
     * @param firstName the first name of the user
     * @param surName the second name of the user.
     * @param role the role of the user.
     */
    public User(String firstName, String surName, String password, Role role) {
        this(null,null,firstName,surName,password, role,null);

    }
    public User(){
        this("","", null,null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null)
            return false;
        if (getUsername() != null ? !getUsername().equals(user.getUsername()) : user.getUsername() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(user.getFirstName()) : user.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(user.getLastName()) : user.getLastName() != null)
            return false;
        if (getRole() != null ? !getRole().equals(user.getRole()) : user.getRole() != null)
            return false;
        return getEid() != null ? getEid().equals(user.getEid()) : user.getEid() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        result = 31 * result + (getEid() != null ? getEid().hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public Long getEid() {
        return eid;
    }

    public String getPassword() { return password;}

    public void setPassword(String password) {this.password = password;}
}
