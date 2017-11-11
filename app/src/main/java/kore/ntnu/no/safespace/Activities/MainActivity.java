package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.tasks.GetUserTask;

public class MainActivity extends AppCompatActivity {

    public static final String USER = "kore.ntnu.no.safespace.activities.MainActivity.USER";
    public static final String URL = "http://192.168.1.10:8080";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText loginUser = (EditText) findViewById(R.id.launch_username);
        EditText loginPwd = (EditText) findViewById(R.id.launch_password);
        Button loginButton = (Button) findViewById(R.id.launch_loginButton);
        loginButton.setOnClickListener((View v) -> {
            // Check input -> login -> start ny activity
            String username = loginUser.getText().toString();
            String password = loginPwd.getText().toString();
            new GetUserTask((result) -> {
                if (result.isSuccess()) {
                    Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
                    intent.putExtra(USER, result.getResult());
                    startActivity(intent);
                } else {
                    System.out.println(result.getError().getMessage());
                }
            }).execute(new UserCredentials(username, password));

        });
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, RegisterUserActivity.class);
            startActivity(intent);
        });


    }
}
