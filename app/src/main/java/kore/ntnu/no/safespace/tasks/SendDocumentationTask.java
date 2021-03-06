package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.ImageUtils;

/**
 * The purpose of this class is to send a Documentation to the server.
 *
 * @author Robert
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
        Documentation documentation = documentations[0];
        try {
            ServiceResult<Documentation> serviceResult = documentationService.add(documentation);
            Documentation newDocumentation = serviceResult.getObject();
            if (documentation.getImages() != null) {
                sendImages(newDocumentation.getId(), documentation.getImages());
            }
            return new AsyncTaskResult<>(newDocumentation);
        } catch (IOException ex) {
            return new AsyncTaskResult<>(ex.getMessage());
        }
    }

    /**
     * Converts an image to a base64 string and adds it to the ImageService so it can be sent to the server.
     * @param documentationId
     * @param images
     * @throws IOException
     */
    private void sendImages(Long documentationId, List<Image> images) throws IOException {
        for (Image image : images) {
            image.setData(Base64.encodeToString(ImageUtils.getRawImageData(image), Base64.NO_WRAP));
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
