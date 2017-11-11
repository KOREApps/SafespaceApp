package kore.ntnu.no.safespace.tasks;

/**
 * Created by Robert on 11-Nov-17.
 */

public interface AsyncOnPostExecute<T> {

    void onPostExecute(AsyncTaskResult<T> result);

}
