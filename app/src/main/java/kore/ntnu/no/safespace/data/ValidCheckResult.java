package kore.ntnu.no.safespace.data;

/**
 * This class defines what a ValidCheckResult object looks like.
 * It is used for retrieving error messages if something goes wrong.
 *
 * @author Robert
 */
public class ValidCheckResult {

    private boolean valid;
    private String message;

    public ValidCheckResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
