package kore.ntnu.no.safespace.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.utils.ImageUtils;

/**
 * Created by Kristoffer on 2017-11-30.
 */

public class ImageLoaderTask extends AsyncTask<Image,Void,Bitmap> {
    private int scale = 8;

    @Override
    protected Bitmap doInBackground(Image... images) {
        try {
            return ImageUtils.getBitmap(images[0], scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnImageRead {
        void onImageRead(Bitmap map);
    }

    OnImageRead callback;
    public ImageLoaderTask(int scaleFactor, OnImageRead callback) throws IOException {
        this.callback = callback;
        this.scale = scaleFactor;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(callback != null){
            callback.onImageRead(bitmap);
        }
    }
}
