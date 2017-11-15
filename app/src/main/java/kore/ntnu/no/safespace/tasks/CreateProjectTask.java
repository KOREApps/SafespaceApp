package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.service.ProjectService;

/**
 * Created by OscarWika on 15.11.2017.
 */

public class CreateProjectTask extends AsyncTask<Project, Integer, AsyncTaskResult<Project>> {

    private AsyncOnPostExecute<Project> callback;
    private ProjectService projectService;

    public CreateProjectTask(AsyncOnPostExecute<Project> callback) {
        this.callback = callback;
        this.projectService = new ProjectService();
    }

    @Override
    protected AsyncTaskResult<Project> doInBackground(Project... projects) {
        Project newProject = projects[0];
        try {
            newProject = projectService.add(newProject);
            return new AsyncTaskResult<Project>(newProject);
        } catch (IOException ex) {
            return new AsyncTaskResult<Project>(null, ex);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Project> result) {
        if (this.callback != null) {
            callback.onPostExecute(result);
        }
    }
}
