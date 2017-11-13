package kore.ntnu.no.safespace.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kore.ntnu.no.safespace.R;

/**
 * Created by OscarWika on 13.11.2017.
 */

public class ProjectActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        EditText projectTitle = findViewById(R.id.projectHeaderText);
        EditText projectDescription = findViewById(R.id.projectDescription);
        Button createProjectBtn = findViewById(R.id.createProjectBtn);

        createProjectBtn.setOnClickListener(view -> {
            Toast.makeText(ProjectActivity.this, "You pressed create LMFAO", Toast.LENGTH_SHORT).show();
        });
    }
}
