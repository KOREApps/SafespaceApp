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

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ImageService;
import kore.ntnu.no.safespace.service.ServiceResult;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by Kristoffer on 2017-11-17.
 */

public class GetDocumentationsTask extends AsyncTask<Void, Integer, AsyncTaskResult<List<Documentation>>> {

    private AsyncOnPostExecute<List<Documentation>> callback;
    private DocumentationService documentationService;
    private ImageService imageService;
    private ProgressDialog dialog;

    public GetDocumentationsTask(AsyncOnPostExecute<List<Documentation>> callback, Context context) {
        this.callback = callback;
        this.documentationService = new DocumentationService();
        this.imageService = new ImageService();
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Fetching latest documentation");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected AsyncTaskResult<List<Documentation>> doInBackground(Void... voids) {
        try {
            ServiceResult<List<Documentation>> serviceResult = documentationService.getAll();
            if(serviceResult != null) {
                for (Documentation d : serviceResult.getObject()) {
                    List<Image> images = new ArrayList<>();
                    for (Image i : documentationService.getImagesForDocumentation(d.getId()).getObject()) {
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
    protected void onPostExecute(AsyncTaskResult<List<Documentation>> result) {
        if (callback != null) {
            callback.onPostExecute(result);
        }
        dialog.dismiss();
    }
}
