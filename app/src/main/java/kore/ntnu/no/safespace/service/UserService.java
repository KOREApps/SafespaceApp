package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Class that handles communication with backend when performing Create, Read or Update operations
 * on users. Has some functions to check if user credentials are valid.
 *
 * @author Robert
 */
public class UserService implements RestClient<User, Long> {

    private static final String URL = IdUtils.URL + "/users";

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
        response = http.postNoAuth(URL, gson.toJson(user));
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

    /**
     * Retrieves a user object for the given user credentials
     * @param userCredentials user credentials to find user object for
     * @return user belonging to given credentials
     */
    public ServiceResult<User> getByCredentials(UserCredentials userCredentials){
        try {
            HttpResponse response = http.post(URL + "/login", gson.toJson(userCredentials));
            ServiceResult<User> serviceResult = null;
            if (response.isSuccess()) {
                serviceResult = new ServiceResult<>(
                        gson.fromJson(response.getResponse(), User.class), true, "OK");
            } else {
                serviceResult = new ServiceResult<>(null, false, "Wrong username or password");
            }
            return serviceResult;
        } catch (IOException ex) {
            return new ServiceResult<>(null, false, ex.getMessage());
        }
    }
    
}
