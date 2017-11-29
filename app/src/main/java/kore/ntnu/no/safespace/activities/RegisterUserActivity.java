package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.tasks.AsyncTaskResult;
import kore.ntnu.no.safespace.tasks.RegisterUserTask;
import kore.ntnu.no.safespace.utils.dialogs.ErrorDialog;

/**
 * The purpose of this activity is to register a new user.
 *
 * @author x
 */
public class RegisterUserActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        usernameText = findViewById(R.id.usernameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        setRegisterButtonOnClick();
    }

    private void setRegisterButtonOnClick(){
        registerButton.setOnClickListener((View view) -> {
            if (isPasswordFieldsEqual()) {
                User newUser = getUser();
                new RegisterUserTask(((AsyncTaskResult<User> result) -> {
                    if (result.isSuccess()) {
                        Intent intent = new Intent(RegisterUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        ErrorDialog.showErrorDialog(this, result.getMessage());
                    }
                })).execute(newUser);
            } else {
                ErrorDialog.showErrorDialog(this, "Password field need to match");
            }
        });
    }

    private boolean isPasswordFieldsEqual(){
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        return password.equals(confirmPassword);
    }

    private User getUser(){
        String username = usernameText.getText().toString();
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        return new User(null, username, firstName, lastName, password, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
