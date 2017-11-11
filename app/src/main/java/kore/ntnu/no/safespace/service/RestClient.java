package kore.ntnu.no.safespace.service;

import java.io.IOException;
import java.util.List;

/**
 * Created by robert on 11/1/17.
 */

public interface RestClient<T, I> {

    public List<T> getAll() throws IOException;

    public T getOne(I id) throws IOException;

    public T add(T t) throws IOException;

    public T update(T t) throws IOException;

}
