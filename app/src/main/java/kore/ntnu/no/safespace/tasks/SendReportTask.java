package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ReportService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.ImageUtils;

/**
 * Created by Robert on 11-Nov-17.
 */

public class SendReportTask extends AsyncTask<IncidentReport, Integer, AsyncTaskResult<IncidentReport>> {

    private AsyncOnPostExecute<IncidentReport> callback;
    private ReportService reportService;
    private ImageService imageService;

    public SendReportTask(AsyncOnPostExecute<IncidentReport> callback){
        this.callback = callback;
        this.reportService = new ReportService();
        this.imageService = new ImageService();
    }

    @Override
    protected AsyncTaskResult<IncidentReport> doInBackground(IncidentReport... incidentReports) {
        IncidentReport report = incidentReports[0];
        try {
            ServiceResult<IncidentReport> serviceResult = reportService.add(report);
            IncidentReport newReport = serviceResult.getObject();
            if (newReport.getImages() != null) {
                sendImages(newReport.getId(), report.getImages());
            }
            return new AsyncTaskResult<>(newReport);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(ex.getMessage());
        }
    }

    private void sendImages(Long reportId, List<Image> images) throws IOException {
        for (Image image : images) {
            image.setData(Base64.encodeToString(ImageUtils.getRawImageData(image), Base64.NO_WRAP));
            image.setReport(new IncidentReport(reportId, null, null));
            image.setFileExtension(image.getFileExtension().replace(".", ""));
            imageService.add(image);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<IncidentReport> incidentReportAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(incidentReportAsyncTaskResult);
        }
    }
}
