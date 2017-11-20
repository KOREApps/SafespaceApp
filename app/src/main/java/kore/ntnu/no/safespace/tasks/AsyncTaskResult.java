package kore.ntnu.no.safespace.tasks;

/**
 * Created by Robert on 11-Nov-17.
 */

public class AsyncTaskResult<T> {

    private T result = null;
    private String message = null;
    private boolean success;

    public AsyncTaskResult(T result) {
        this.result = result;
        this.success = true;
    }

    public AsyncTaskResult(String message) {
        this.message = message;
        this.success = false;
    }

    public AsyncTaskResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public AsyncTaskResult(T result, String message, boolean success) {
        this.result = result;
        this.message = message;
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
