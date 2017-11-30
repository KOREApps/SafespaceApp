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

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * The purpose of this class is to retrieve all documents from the server.
 *
 * @author Robert
 */
public class GetDocumentationsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<Documentation>>> {

    private AsyncOnPostExecute<List<Documentation>> callback;
    private DocumentationService documentationService;
    private ImageService imageService;

    public GetDocumentationsTask(AsyncOnPostExecute<List<Documentation>> callback, Context context) {
        this.callback = callback;
        this.documentationService = new DocumentationService();
        this.imageService = new ImageService();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected AsyncTaskResult<List<Documentation>> doInBackground(Void... voids) {
        try {
            ServiceResult<List<Documentation>> serviceResult = documentationService.getAll();
            if(serviceResult != null) {
                loadImagesForDocumentations(serviceResult.getObject());
            } else {
                Log.e(GetDocumentationsTask.class.getSimpleName(), "Failed to get Documentation");
            }
            return new AsyncTaskResult<>(serviceResult.getObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new AsyncTaskResult<>(Collections.EMPTY_LIST);
    }

    private void loadImagesForDocumentations(List<Documentation> documentations) throws IOException {
        for (Documentation d : documentations) {
            ServiceResult<List<Image>> images = documentationService.getImagesForDocumentation(d.getId());
            d.setImages(loadDataForImages(images.getObject()));
        }
    }

    private List<Image> loadDataForImages(List<Image> images) throws IOException {
        List<Image> imgs = new ArrayList<>();
        for (Image i : images) {
            byte[] rawData = imageService.getImageData(i);
            File image = StorageUtils.saveToDisk(rawData, i.getName(), i.getFileExtension());
            imgs.add(new Image(image));
        }
        return imgs;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<Documentation>> result) {
        if (callback != null) {
            callback.onPostExecute(result);
        }
    }
}
