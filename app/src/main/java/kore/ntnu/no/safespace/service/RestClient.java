package kore.ntnu.no.safespace.service;

import java.io.IOException;
import java.util.List;

/**
 * Interface for classes that communicate with backend with Create, Read and Update operations
 * Mostly used to keep the methods and parameters consistent
 * @author Robert
 */
public interface RestClient<T, I> {

    /**
     * Retrives all objects of type T from backend
     * @return List of objects of type T
     * @throws IOException error when contacting backend
     */
    public ServiceResult<List<T>> getAll() throws IOException;

    /**
     * Retrieved one object of type T with given id
     * @param id id of object to retrieve
     * @return object of type T with given id
     * @throws IOException error when contacting backend
     */
    public ServiceResult<T> getOne(I id) throws IOException;

    /**
     * Adds one object of type T
     * @param t object to add
     * @return object returned from backend after creation
     * @throws IOException error when contacting backend
     */
    public ServiceResult<T> add(T t) throws IOException;

    /**
     * !!! NOT IN USE !!!
     * To be used for update operations.
     */
    public ServiceResult<T> update(T t) throws IOException;

}
