package kore.ntnu.no.safespace.service;

/**
 * Class description..
 *
 * @author Robert
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
