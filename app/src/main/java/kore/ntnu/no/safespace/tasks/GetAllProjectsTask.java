package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.service.ProjectService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Created by robert on 11/13/17.
 */

public class GetAllProjectsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<Project>>> {

    private AsyncOnPostExecute<List<Project>> callback;
    private ProjectService projectService;

    public GetAllProjectsTask(AsyncOnPostExecute<List<Project>> callback) {
        this.callback = callback;
        this.projectService = new ProjectService();
    }

    @Override
    protected AsyncTaskResult<List<Project>> doInBackground(Void... voids) {
        try {
            ServiceResult<List<Project>> serviceResult = projectService.getAll();
            return new AsyncTaskResult<>(serviceResult.getObject());
        } catch (IOException ex) {
            return new AsyncTaskResult<>(null, ex);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<Project>> listAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(listAsyncTaskResult);
        }
    }
}
