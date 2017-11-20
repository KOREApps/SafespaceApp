package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kore.ntnu.no.safespace.ErrorDialog;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.tasks.GetUserTask;

public class MainActivity extends AppCompatActivity {

    public static final String USER = "kore.ntnu.no.safespace.activities.MainActivity.USER";

    public static final String USERNAME = "kore.ntnu.no.safespace.activities.MainActivity.USERNAME";
    public static final String PASSWORD = "kore.ntnu.no.safespace.activities.MainActivity.PASSWORD";

    public static final String URL = "https://roberris-ss.uials.no:8080";
    // public static final String URL = "https://158.38.198.168:8080";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearCredentialsInSharedPreferences();
        setContentView(R.layout.activity_main);

        EditText loginUser = findViewById(R.id.launch_username);
        EditText loginPwd = findViewById(R.id.launch_password);
        Button loginButton =  findViewById(R.id.launch_loginButton);
        loginButton.setOnClickListener((View v) -> {
            // Check input -> login -> start ny activity
            final String username = loginUser.getText().toString();
            final String password = loginPwd.getText().toString();
            loginUser.setText("");
            loginPwd.setText("");
            storeCredentialsInSharedPreferences(username, password);
            new GetUserTask((result) -> {
                if (result.isSuccess() && result.getResult() != null) {
                    Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                    intent.putExtra(USER, result.getResult());
                    startActivity(intent);
                } else {
                    ErrorDialog.showErrorDialog(this, result.getMessage());
                }
            }).execute(new UserCredentials(username, password));
        });
        Button anonLoginButton = findViewById(R.id.anonLoginButton);
        anonLoginButton.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
            intent.putExtra(USER, new User());
            startActivity(intent);
        });
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, RegisterUserActivity.class);
            startActivity(intent);
        });
    }

    private void clearCredentialsInSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, "");
        editor.putString(PASSWORD, "");
        editor.apply();
    }

    private void storeCredentialsInSharedPreferences(String username, String password){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

}
