package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.ReportService;

/**
 * Created by Kristoffer on 2017-11-17.
 */

public class GetReportsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<IncidentReport>>> {

    private AsyncOnPostExecute<List<IncidentReport>> callback;
    private ReportService reportService;

    public GetReportsTask(AsyncOnPostExecute<List<IncidentReport>> callback) {
        this.callback = callback;
        this.reportService = new ReportService();
    }

    @Override
    protected AsyncTaskResult<List<IncidentReport>> doInBackground(Void... voids) {
        try {
            return new AsyncTaskResult<>(reportService.getAll());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AsyncTaskResult<>(Collections.EMPTY_LIST);
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<IncidentReport>> result) {
        if (callback != null) {
            callback.onPostExecute(result);
        }
    }
}
