package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.activities.MainActivity;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;

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
    public ServiceResult<List<User>> getAll() {
        try {
            HttpResponse response = http.get(URL);
            List<User> users = gson.fromJson(response.getResponse(), LIST_TYPE);
            ServiceResult<List<User>> serviceResult = new ServiceResult<>(users, true, "OK");
            return serviceResult;
        } catch (IOException e) {
            Log.e(UserService.class.getSimpleName(), "Failed to fetch users");
            return new ServiceResult<>(Collections.emptyList(), false, e.getMessage());
        }
    }

    @Override
    public ServiceResult<User> getOne(Long id) {
        try {
            HttpResponse response = http.get(URL + "/" + id);
            User user = gson.fromJson(response.getResponse(), User.class);
            ServiceResult<User> serviceResult = new ServiceResult<>(user, true, "OK");
            return serviceResult;
        } catch (IOException e) {
            Log.e(UserService.class.getSimpleName(), "Failed to fetch user with id: " + id);
            return new ServiceResult<>(null, false, e.getMessage());
        }
    }

    @Override
    public ServiceResult<User> add(User user) throws IOException {
        HttpResponse response = null;
        response = http.post(URL, gson.toJson(user));
        if (response.getCode() == 200) {
            ServiceResult<User> serviceResult = new ServiceResult<>(
                    gson.fromJson(response.getResponse(), User.class), true, "OK");
            return serviceResult;
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }

    }

    @Override
    public ServiceResult<User> update(User user) {
        return null;
    }

    public ServiceResult<User> getByCredentials(UserCredentials userCredentials){
        try {
            HttpResponse response = http.post(URL + "/login", gson.toJson(userCredentials));
            ServiceResult<User> serviceResult = new ServiceResult<>(
                    gson.fromJson(response.getResponse(), User.class), true, "OK");
            return serviceResult;
        } catch (IOException ex) {
            return null;
        }
    }

    private boolean isResponseACheckResult(String response){
        try {
            gson.fromJson(response, ValidCheckResult.class);
            return true;
        } catch (JsonParseException ex) {
            return false;
        }
    }
}
