package kore.ntnu.no.safespace.data;

/**
 * Created by Robert on 11-Nov-17.
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
