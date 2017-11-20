package kore.ntnu.no.safespace.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.ImageUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by Robert on 11-Nov-17.
 */

public class SendDocumentationTask extends AsyncTask<Documentation, Integer, AsyncTaskResult<Documentation>> {

    private AsyncOnPostExecute<Documentation> callback;
    private DocumentationService documentationService;
    private ImageService imageService;

    public SendDocumentationTask(AsyncOnPostExecute<Documentation> callback){
        this.callback = callback;
        this.documentationService = new DocumentationService();
        this.imageService = new ImageService();
    }

    @Override
    protected AsyncTaskResult<Documentation> doInBackground(Documentation... documentations) {
        Documentation report = documentations[0];
        try {
            ServiceResult<Documentation> serviceResult = documentationService.add(report);
            Documentation newReport = serviceResult.getObject();
            if (report.getImages() != null) {
                sendImages(newReport.getId(), report.getImages());
            }
            return new AsyncTaskResult<>(newReport);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(ex.getMessage());
        }
    }

    private void sendImages(Long documentationId, List<Image> images) throws IOException {
        for (Image image : images) {
            image.setData(ImageUtils.getB64ImageData(image));
            image.setDocumentation(new Documentation(documentationId, null, null, null));
            image.setFileExtension(image.getFileExtension().replace(".", ""));
            imageService.add(image);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Documentation> incidentReportAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(incidentReportAsyncTaskResult);
        }
    }
}
