package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.tasks.CreateProjectTask;

/**
 * Created by Oscar Wika on 13.11.2017.
 */

public class ProjectActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setCreateButtonOnClick();

        EditText projectTitleField = findViewById(R.id.projectHeaderText);
        EditText projectDescriptionField = findViewById(R.id.projectDescription);
        projectTitleField.setFocusableInTouchMode(true);
        projectTitleField.requestFocus();
    }

    private void setCreateButtonOnClick(){
        Button createButton = findViewById(R.id.createProjectBtn);
        createButton.setOnClickListener((View view) -> {
            Project newProject = getProject();
            new CreateProjectTask((result -> {
                if (result.isSuccess()) {
                    Toast.makeText(ProjectActivity.this, "Project successfully created", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println(result.getError().getMessage());
                }
            })).execute(newProject);
        });
    }

    public Project getProject() {
        EditText projectTitleField = findViewById(R.id.projectHeaderText);
        String projectTitle = projectTitleField.getText().toString();
        EditText projectDescriptionField = findViewById(R.id.projectDescription);
        String projectDescription = projectDescriptionField.getText().toString();
        if (projectTitle.length() > 2) {
            return new Project(projectTitle, projectDescription);
        } else {
            Toast.makeText(this, "Please enter a project Title", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
