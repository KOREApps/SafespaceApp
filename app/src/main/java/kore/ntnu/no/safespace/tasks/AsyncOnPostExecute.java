package kore.ntnu.no.safespace.tasks;

/**
 * Class description..
 *
 * @author Robert
 */
public interface AsyncOnPostExecute<T> {

    void onPostExecute(AsyncTaskResult<T> result);

}
