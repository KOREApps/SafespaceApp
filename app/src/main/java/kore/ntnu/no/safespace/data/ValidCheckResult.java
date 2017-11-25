package kore.ntnu.no.safespace.data;

/**
 * Class description..
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
