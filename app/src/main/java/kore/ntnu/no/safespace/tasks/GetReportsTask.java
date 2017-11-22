package kore.ntnu.no.safespace.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.ReportService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Created by Kristoffer on 2017-11-17.
 */

public class GetReportsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<IncidentReport>>> {

    private AsyncOnPostExecute<List<IncidentReport>> callback;
    private ReportService reportService;
    private ProgressDialog dialog;

    public GetReportsTask(AsyncOnPostExecute<List<IncidentReport>> callback, Context context) {
        this.callback = callback;
        this.reportService = new ReportService();
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Fetching latest reports");
        dialog.show();
        super.onPreExecute();
    }


    @Override
    protected AsyncTaskResult<List<IncidentReport>> doInBackground(Void... voids) {
        try {
            ServiceResult<List<IncidentReport>> serviceResult = reportService.getAll();
            return new AsyncTaskResult<>(serviceResult.getObject());
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
        dialog.dismiss();
    }
}
