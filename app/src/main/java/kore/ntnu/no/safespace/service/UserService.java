package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.activities.MainActivity;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;

/**
 * Created by robert on 11/1/17.
 */

public class UserService implements RestClient<User, Long> {

    private static final String URL = MainActivity.URL + "/users";

    private static final Type LIST_TYPE = new TypeToken<List<User>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public UserService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public List<User> getAll() {
        try {
            String response = http.get(URL);
            List<User> users = gson.fromJson(response, LIST_TYPE);
            return users;
        } catch (IOException e) {
            Log.e(UserService.class.getSimpleName(), "Failed to fetch users");
        }
        return Collections.emptyList();
    }

    @Override
    public User getOne(Long id) {
        try {
            String response = http.get(URL + "/" + id);
            User user = gson.fromJson(response, User.class);
            return user;
        } catch (IOException e) {
            Log.e(UserService.class.getSimpleName(), "Failed to fetch user with id: " + id);
        }
        return null;
    }

    @Override
    public User add(User user) {
        try {
            String response = http.post(URL, gson.toJson(user));
            User newUser = gson.fromJson(response, User.class);
            return newUser;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public User update(User user) {
        return null;
    }

    public User getByCredentials(UserCredentials userCredentials){
        try {
            String response = http.post(URL + "/login", gson.toJson(userCredentials));
            return gson.fromJson(response, User.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
