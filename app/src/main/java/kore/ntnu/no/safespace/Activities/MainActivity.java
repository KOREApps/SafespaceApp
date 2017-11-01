package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kore.ntnu.no.safespace.R;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://158.38.198.168:8080";

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
            Intent intent = new Intent(MainActivity.this, MainNavigationMenuActivity.class);
            startActivity(intent);
        });


    }
}
