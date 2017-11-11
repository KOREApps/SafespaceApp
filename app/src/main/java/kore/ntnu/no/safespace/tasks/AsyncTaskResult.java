package kore.ntnu.no.safespace.tasks;

/**
 * Created by Robert on 11-Nov-17.
 */

public class AsyncTaskResult<T> {

    private T result;
    private Throwable error = null;

    public AsyncTaskResult(T result) {
        this.result = result;
    }

    public AsyncTaskResult(T result, Throwable error) {
        this.result = result;
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }
}
