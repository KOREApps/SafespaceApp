package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.service.UserService;

/**
 * Class description..
 *
 * @author Robert
 */
public class RegisterUserTask extends AsyncTask<User, Integer, AsyncTaskResult<User>> {

    private AsyncOnPostExecute<User> callback;
    private UserService userService;

    public RegisterUserTask(AsyncOnPostExecute<User> callback) {
        this.callback = callback;
        this.userService = new UserService();
    }

    @Override
    protected AsyncTaskResult<User> doInBackground(User... users) {
        User newUser = users[0];
        try {
            ServiceResult<User> serviceResult = userService.add(newUser);
            newUser = serviceResult.getObject();
            return new AsyncTaskResult<>(newUser);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(ex.getMessage());
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<User> result) {
        if (this.callback != null) {
            callback.onPostExecute(result);
        }
    }
}
