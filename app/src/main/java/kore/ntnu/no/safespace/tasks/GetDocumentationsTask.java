package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Created by Kristoffer on 2017-11-17.
 */

public class GetDocumentationsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<Documentation>>> {

    private AsyncOnPostExecute<List<Documentation>> callback;
    private DocumentationService documentationService;

    public GetDocumentationsTask(AsyncOnPostExecute<List<Documentation>> callback) {
        this.callback = callback;
        this.documentationService = new DocumentationService();
    }

    @Override
    protected AsyncTaskResult<List<Documentation>> doInBackground(Void... voids) {
        try {
            ServiceResult<List<Documentation>> serviceResult = documentationService.getAll();
            return new AsyncTaskResult<>(serviceResult.getObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AsyncTaskResult<>(Collections.EMPTY_LIST);
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<Documentation>> result) {
        if (callback != null) {
            callback.onPostExecute(result);
        }
    }
}
