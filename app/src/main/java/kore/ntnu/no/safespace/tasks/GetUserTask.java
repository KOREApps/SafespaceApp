package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.service.UserService;

/**
 * Created by Robert on 11-Nov-17.
 */

public class GetUserTask extends AsyncTask<UserCredentials, Integer, AsyncTaskResult<User>> {

    private AsyncOnPostExecute<User> callback;
    private UserService userService;

    public GetUserTask(AsyncOnPostExecute<User> callback) {
        this.callback = callback;
        this.userService = new UserService();
    }

    @Override
    protected AsyncTaskResult<User> doInBackground(UserCredentials... credentials) {
        UserCredentials userCredentials = credentials[0];
        ServiceResult<User> serviceResult = userService.getByCredentials(userCredentials);
        return new AsyncTaskResult<>(serviceResult.getObject(), serviceResult.getMessage(), serviceResult.isSuccess());
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<User> result) {
        if (callback != null) {
            callback.onPostExecute(result);
        }
    }
}
