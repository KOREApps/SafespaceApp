package kore.ntnu.no.safespace.data;

/**
 * This class defines what a UserCredentials object looks like.
 * It is used for checking the users credentials.
 *
 * @author Robert
 */
public class UserCredentials {

    private String username;
    private String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
