package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.service.ProjectService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * The purpose of this class is to send a Project to the server.
 *
 * @author Oscar
 */
public class SendProjectTask extends AsyncTask<Project, Integer, AsyncTaskResult<Project>> {

    private AsyncOnPostExecute<Project> callback;
    private ProjectService projectService;

    public SendProjectTask(AsyncOnPostExecute<Project> callback) {
        this.callback = callback;
        this.projectService = new ProjectService();
    }

    @Override
    protected AsyncTaskResult<Project> doInBackground(Project... projects) {
        Project project = projects[0];
        try {
            ServiceResult<Project> serviceResult = projectService.add(project);
            Project newProject = serviceResult.getObject();
            return new AsyncTaskResult<>(newProject);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(ex.getMessage());
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
