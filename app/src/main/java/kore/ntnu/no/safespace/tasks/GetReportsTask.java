package kore.ntnu.no.safespace.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ReportService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class GetReportsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<IncidentReport>>> {

    private AsyncOnPostExecute<List<IncidentReport>> callback;
    private ReportService reportService;
    private ImageService imageService;
    private ProgressDialog dialog;

    public GetReportsTask(AsyncOnPostExecute<List<IncidentReport>> callback, Context context) {
        this.callback = callback;
        this.reportService = new ReportService();
        this.imageService = new ImageService();
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
            if(serviceResult != null) {
                for (IncidentReport d : serviceResult.getObject()) {
                    List<Image> images = new ArrayList<>();
                    for (Image i : reportService.getImagesForReport(d.getId()).getObject()) {
                        byte[] rawData = imageService.getImageData(i);
                        File image = StorageUtils.saveToDisk(rawData, i.getName(), i.getFileExtension());
                        images.add(new Image(image));
                    }
                    d.setImages(images);
                }
            } else {
                Log.e(GetDocumentationsTask.class.getSimpleName(), "Failed to get Documentation");
            }
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
