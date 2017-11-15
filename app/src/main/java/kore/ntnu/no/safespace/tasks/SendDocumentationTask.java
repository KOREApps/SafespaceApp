package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.service.DocumentationService;

/**
 * Created by Robert on 11-Nov-17.
 */

public class SendDocumentationTask extends AsyncTask<Documentation, Integer, AsyncTaskResult<Documentation>> {

    private AsyncOnPostExecute<Documentation> callback;
    private DocumentationService documentationService;

    public SendDocumentationTask(AsyncOnPostExecute<Documentation> callback){
        this.callback = callback;
        this.documentationService = new DocumentationService();
    }

    @Override
    protected AsyncTaskResult<Documentation> doInBackground(Documentation... documentations) {
        Documentation newReport = documentations[0];
        try {
            newReport = documentationService.add(newReport);
            return new AsyncTaskResult<>(newReport);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(null, ex);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Documentation> incidentReportAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(incidentReportAsyncTaskResult);
        }
    }
}
