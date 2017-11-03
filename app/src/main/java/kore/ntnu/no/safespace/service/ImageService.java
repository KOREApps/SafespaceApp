package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.Activities.MainActivity;
import kore.ntnu.no.safespace.Data.Image;
import okhttp3.MediaType;

/**
 * Created by robert on 11/2/17.
 */

public class ImageService implements RestClient<Image, Long> {

    private static final String IMAGE_URL = MainActivity.URL + "/images";
    private static Type LIST_TYPE = new TypeToken<List<Image>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ImageService() {
        http = new HttpService();
        gson = new Gson();
    }

    @Override
    public List<Image> getAll() {
        try {
            String response = http.get(IMAGE_URL);
            List<Image> images = gson.fromJson(response, LIST_TYPE);
            return images;
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch images");
            return Collections.emptyList();
        }
    }

    @Override
    public Image getOne(Long id) {
        try {
            String response = http.get(IMAGE_URL + "/" + id);
            return gson.fromJson(response, Image.class);
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch image");
            return null;
        }
    }

    public Image getOneWithData(Long id) {
        try {
            String response = http.get(IMAGE_URL + "/" + id + "?data=true");
            return gson.fromJson(response, Image.class);
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch image");
            return null;
        }
    }

    @Override
    public Image add(Image image) {
        try {
            String body = gson.toJson(image);
            String response = http.post(IMAGE_URL, body);
            return gson.fromJson(response, Image.class);
        } catch (IOException ex) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image");
            return null;
        }
    }

    @Override
    public Image update(Image image) {
        return null;
    }

    public void addImageData(Image image, byte[] data) {
        try {
            final String url = IMAGE_URL + "/data/" + image.getId();
            String response = http.post(url, data, getMediaType(image));
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image data");
        }
    }

    public byte[] getImageData(Image image) {
        try {
            final String url = IMAGE_URL + "/data/" + image.getId();
             return http.get(url).getBytes();
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image data");
            return new byte[0];
        }
    }

    private MediaType getMediaType(Image image) {
        if (image.getFileExtension().equals("jpg") || image.getFileExtension().equals("jpeg")) {
            return HttpService.JPG;
        } else if (image.getFileExtension().equals("png")) {
            return HttpService.PNG;
        } else {
            return HttpService.BYTES;
        }
    }

}
