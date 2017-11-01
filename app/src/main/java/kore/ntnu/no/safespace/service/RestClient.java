package kore.ntnu.no.safespace.service;

import java.util.List;

/**
 * Created by robert on 11/1/17.
 */

public interface RestClient<T, I> {

    public List<T> getAll();

    public T getOne(I id);

    public T add(T t);

    public T update(T t);

}
