package kore.ntnu.no.safespace.service;

import java.io.IOException;
import java.util.List;

/**
 * Created by robert on 11/1/17.
 */

public interface RestClient<T, I> {

    public ServiceResult<List<T>> getAll() throws IOException;

    public ServiceResult<T> getOne(I id) throws IOException;

    public ServiceResult<T> add(T t) throws IOException;

    public ServiceResult<T> update(T t) throws IOException;

}
