package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kore.ntnu.no.safespace.utils.dialogs.ErrorDialog;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.tasks.AsyncTaskResult;
import kore.ntnu.no.safespace.tasks.RegisterUserTask;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setRegisterButtonOnClick();
    }

    private void setRegisterButtonOnClick(){
        Button registerButton = findViewById(R.id.registerButton);
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
        EditText passwordInput = findViewById(R.id.passwordInput);
        String password = passwordInput.getText().toString();
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        String confirmPassword = confirmPasswordInput.getText().toString();
        return password.equals(confirmPassword);
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
