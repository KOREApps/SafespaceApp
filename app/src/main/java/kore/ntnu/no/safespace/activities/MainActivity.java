package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import kore.ntnu.no.safespace.BCrypt;
import kore.ntnu.no.safespace.ErrorDialog;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.tasks.GetLocationTask;
import kore.ntnu.no.safespace.tasks.GetUserTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;

public class MainActivity extends AppCompatActivity {
    // public static final String URL = "https://158.38.198.168:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        new GetLocationTask((result -> {
//        System.out.println(result.getResult().getAccuracy());
//        })).execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText loginUser = findViewById(R.id.launch_username);
        EditText loginPwd = findViewById(R.id.launch_password);
        Button loginButton =  findViewById(R.id.launch_loginButton);
        String lastUser = getLastUser();
        loginUser.setText(lastUser);
        loginButton.setOnClickListener((View v) -> {
            // Check input -> login -> start ny activity
            final String username = loginUser.getText().toString();
            final String password = loginPwd.getText().toString();
            loginPwd.setText("");
            storeCredentialsInSharedPreferences(username, password);
            login(username, password);
        });
        Button anonLoginButton = findViewById(R.id.anonLoginButton);
        anonLoginButton.setOnClickListener((view) -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            login(prefs.getString(IdUtils.USERNAME, ""), prefs.getString(IdUtils.PASSWORD, ""));
        });
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, RegisterUserActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        if(ConnectionUtil.isConnected(this)){
            new GetUserTask((result) -> {
                if (result.isSuccess() && result.getResult() != null) {
                    storeUser(result.getResult());
                    Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                    intent.putExtra(IdUtils.USER, result.getResult());
                    startActivity(intent);
                } else {
                    ErrorDialog.showErrorDialog(this, result.getMessage());
                }
            }).execute(new UserCredentials(username, password));
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String jsonUser = prefs.getString(IdUtils.USER, "");
            User storedUser = new Gson().fromJson(jsonUser, User.class);

            //TODO: get hash type or something (Bcrypt)
            boolean result = BCrypt.checkpw(password, storedUser.getPassword());
            if(storedUser.getUsername().equals(username) && result){
                Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                intent.putExtra(IdUtils.USER, storedUser);
                startActivity(intent);
            }
        }
    }
    private void storeUser(User loggedInUser) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonUser = gson.toJson(loggedInUser);
        editor.putString(IdUtils.USER, jsonUser);
        editor.apply();
    }

    private String getLastUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(IdUtils.USERNAME, "");
    }

    private void clearCredentialsInSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(IdUtils.USERNAME, "");
        editor.putString(IdUtils.PASSWORD, "");
        editor.apply();
    }

    private void storeCredentialsInSharedPreferences(String username, String password){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(IdUtils.USERNAME, username);
        editor.putString(IdUtils.PASSWORD, password);
        editor.apply();
    }

}
