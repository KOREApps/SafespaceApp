package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.service.UserService;

/**
 * Created by Robert on 11-Nov-17.
 */

public class GetUserTask extends AsyncTask<UserCredentials, Integer, User> {

    public interface OnPostExecute{
        void onPostExecute(User user);
    }

    private OnPostExecute callback;
    private UserService userService;

    public GetUserTask(OnPostExecute callback) {
        this.callback = callback;
        this.userService = new UserService();
    }

    @Override
    protected User doInBackground(UserCredentials... credentials) {
        UserCredentials userCredentials = credentials[0];
        return userService.getByCredentials(userCredentials);
    }

    @Override
    protected void onPostExecute(User user) {
        if (callback != null) {
            callback.onPostExecute(user);
        }
    }
}
