package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import kore.ntnu.no.safespace.data.BugReport;
import kore.ntnu.no.safespace.service.BugReportService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Created by robert on 11/26/17.
 */

public class SendBugReportTask extends AsyncTask<BugReport, Integer, AsyncTaskResult<BugReport>> {

    private BugReportService bugReportService;
    private AsyncOnPostExecute<BugReport> callback;

    public SendBugReportTask(AsyncOnPostExecute<BugReport> callback) {
        this.bugReportService = new BugReportService();
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult doInBackground(BugReport... bugReports) {
        BugReport bugReport = bugReports[0];
        try {
            ServiceResult<BugReport> serviceResult = bugReportService.add(bugReport);
            if (serviceResult.isSuccess()) {
                return new AsyncTaskResult<>(serviceResult.getObject(), "OK", true);
            } else {
                return new AsyncTaskResult<>(null, serviceResult.getMessage(), false);
            }
        } catch (IOException e) {
            Log.e(SendBugReportTask.class.getSimpleName(), e.getMessage(), e);
            return new AsyncTaskResult<>(null, e.getMessage(), false);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<BugReport> asyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(asyncTaskResult);
        }
    }
}
