package kore.ntnu.no.safespace.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.service.LocationService;
import kore.ntnu.no.safespace.tasks.GetUserTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.dialogs.ErrorDialog;
import kore.ntnu.no.safespace.utils.hashfunctions.BCrypt;

/**
 * The purpose of this activity is to serve as the entry point of the application.
 * It is where the user can login to the application.
 *
 * @author KORE
 */
public class MainActivity extends AppCompatActivity {

    private EditText loginUser;
    private EditText loginPwd;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!runtime_permission()) {
            startService();
        }

        loginUser = findViewById(R.id.launch_username);
        loginPwd = findViewById(R.id.launch_password);
        loginButton = findViewById(R.id.launch_loginButton);
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
        disableLoginBtn();
        if(ConnectionUtil.isConnected(this)){
            new GetUserTask((result) -> {
                if (result.isSuccess() && result.getResult() != null) {
                    storeUser(result.getResult());
                    Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                    intent.putExtra(IdUtils.USER, result.getResult());
                    startActivity(intent);
                } else {
                    ErrorDialog.showErrorDialog(this, result.getMessage());
                    enableLoginBtn();
                }
            }).execute(new UserCredentials(username, password));
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String jsonUser = prefs.getString(IdUtils.USER, "");
            User storedUser = new Gson().fromJson(jsonUser, User.class);

            boolean result = BCrypt.checkpw(password, storedUser.getPassword());
            if(storedUser.getUsername().equals(username) && result){
                Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                intent.putExtra(IdUtils.USER, storedUser);
                disableLoginBtn();
                ErrorDialog.dismissErrorDialog(getApplicationContext());
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

    private void startService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);
    }

    private boolean runtime_permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startService();
            } else {
                runtime_permission();
            }
        }
    }

    private void enableLoginBtn() {loginButton.setEnabled(true);}

    private void disableLoginBtn() {loginButton.setEnabled(false);}

    @Override
    protected void onResume() {
        loginButton.setEnabled(true);
        super.onResume();
    }


}
