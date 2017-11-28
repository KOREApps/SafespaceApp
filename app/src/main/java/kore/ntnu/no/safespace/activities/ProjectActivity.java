package kore.ntnu.no.safespace.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.tasks.SendProjectTask;

/**
 * The purpose of this activity is to create a new project that the users can connect to their reports/documents.
 *
 * @author x
 */
public class ProjectActivity extends AppCompatActivity {

    private EditText projectTitleField;
    private EditText projectDescriptionField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setCreateButtonOnClick();

        projectTitleField = findViewById(R.id.projectHeaderText);
        projectTitleField.setFocusableInTouchMode(true);
        projectTitleField.requestFocus();
        projectDescriptionField = findViewById(R.id.projectDescription);
    }

    private void setCreateButtonOnClick(){
        Button createButton = findViewById(R.id.createProjectBtn);
        createButton.setOnClickListener((View view) -> {
            Project newProject = getProject();
            new SendProjectTask((result -> {
                if (result.isSuccess()) {
                    Toast.makeText(ProjectActivity.this, "Project successfully created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    System.out.println(result.getMessage());
                }
            })).execute(newProject);
        });
    }

    public Project getProject() {
        String projectTitle = projectTitleField.getText().toString();
        String projectDescription = projectDescriptionField.getText().toString();
        if (projectTitle.length() > 2) {
            return new Project(projectTitle, projectDescription);
        } else {
            Toast.makeText(this, "Please enter a project Title", Toast.LENGTH_SHORT).show();
            return null;
        }
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
