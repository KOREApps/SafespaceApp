package kore.ntnu.no.safespace.service;

/**
 * Created by robert on 11/20/17.
 */

public class ServiceResult<T> {

    private T object;
    private boolean success;
    private String message;

    public ServiceResult(T object, boolean success, String message) {
        this.object = object;
        this.success = success;
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
