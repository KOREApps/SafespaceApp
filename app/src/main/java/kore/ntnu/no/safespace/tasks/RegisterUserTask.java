package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.service.UserService;

/**
 * Created by Robert on 11-Nov-17.
 */

public class RegisterUserTask extends AsyncTask<User, Integer, User> {

    public interface OnPostExecute {
        void onPostExecute(User user);
    }

    private OnPostExecute callback;
    private UserService userService;

    public RegisterUserTask(OnPostExecute callback) {
        this.callback = callback;
        this.userService = new UserService();
    }

    @Override
    protected User doInBackground(User... users) {
        User newUser = users[0];
        newUser = userService.add(newUser);
        return newUser;
    }

    @Override
    protected void onPostExecute(User user) {
        if (this.callback != null) {
            callback.onPostExecute(user);
        }
    }
}
