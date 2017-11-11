package kore.ntnu.no.safespace.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.tasks.RegisterUserTask;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setRegisterButtonOnClick();
    }

    private void setRegisterButtonOnClick(){
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener((View view) -> {
            User newUser = getUser();
            new RegisterUserTask((user -> {
                System.out.println(user.getId());
            })).execute(newUser);
        });
    }

    private User getUser(){
        EditText usernameText = findViewById(R.id.usernameInput);
        String username = usernameText.getText().toString();
        EditText firstNameInput = findViewById(R.id.firstNameInput);
        String firstName = firstNameInput.getText().toString();
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        String lastName = lastNameInput.getText().toString();
        EditText passwordInput = findViewById(R.id.passwordInput);
        String password = passwordInput.getText().toString();
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (password.equals(confirmPassword)) {
            return new User(null, username, firstName, lastName, password, null, null);
        } else {
            return null;
        }
    }
}
