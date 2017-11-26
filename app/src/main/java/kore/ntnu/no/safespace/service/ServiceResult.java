package kore.ntnu.no.safespace.service;

/**
 * Class that represents a result returned by a service class. Wraps the object to be returned.
 * Used so that errors messages can be returned if something goes wrong when service is performing
 * an operation
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
