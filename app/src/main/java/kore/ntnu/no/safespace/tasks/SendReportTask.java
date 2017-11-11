package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.ReportService;

/**
 * Created by Robert on 11-Nov-17.
 */

public class SendReportTask extends AsyncTask<IncidentReport, Integer, AsyncTaskResult<IncidentReport>> {

    private AsyncOnPostExecute<IncidentReport> callback;
    private ReportService reportService;

    public SendReportTask(AsyncOnPostExecute<IncidentReport> callback){
        this.callback = callback;
        this.reportService = new ReportService();
    }

    @Override
    protected AsyncTaskResult<IncidentReport> doInBackground(IncidentReport... incidentReports) {
        IncidentReport newReport = incidentReports[0];
        try {
            newReport = reportService.add(newReport);
            return new AsyncTaskResult<IncidentReport>(newReport);
        } catch (IOException ex) {
            return new AsyncTaskResult<IncidentReport>(null, ex);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<IncidentReport> incidentReportAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(incidentReportAsyncTaskResult);
        }
    }
}
