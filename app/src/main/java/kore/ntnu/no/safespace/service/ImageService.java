package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;
import okhttp3.MediaType;

/**
 * Created by robert on 11/2/17.
 */

public class ImageService implements RestClient<Image, Long> {

    private static final String IMAGE_URL = IdUtils.URL + "/images";
    private static Type LIST_TYPE = new TypeToken<List<Image>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ImageService() {
        http = new HttpService();
        gson = new Gson();
    }

    @Override
    public ServiceResult<List<Image>> getAll() {
        try {
            HttpResponse response = http.get(IMAGE_URL);
            List<Image> images = gson.fromJson(response.getResponse(), LIST_TYPE);
            ServiceResult<List<Image>> serviceResult = new ServiceResult<>(images, true, "OK");
            return serviceResult;
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch images");
            return new ServiceResult<List<Image>>(Collections.emptyList(), false, e.getMessage());
        }
    }

    @Override
    public ServiceResult<Image> getOne(Long id) {
        try {
            HttpResponse response = http.get(IMAGE_URL + "/" + id);
            ServiceResult<Image> serviceResult = new ServiceResult<>(
                    gson.fromJson(response.getResponse(), Image.class), true, "OK");
            return serviceResult;
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch image");
            return null;
        }
    }

    public ServiceResult<Image> getOneWithData(Long id) {
        try {
            HttpResponse response = http.get(IMAGE_URL + "/" + id + "?data=true");
            ServiceResult<Image> serviceResult = new ServiceResult<>(
                    gson.fromJson(response.getResponse(), Image.class), true, "OK");
            return serviceResult;
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to fetch image");
            return null;
        }
    }

    @Override
    public ServiceResult<Image> add(Image image) {
        try {
            String body = gson.toJson(image);
            HttpResponse response = http.post(IMAGE_URL, body);
            ServiceResult<Image> serviceResult = new ServiceResult<>(
                    gson.fromJson(response.getResponse(), Image.class), true, "OK");
            return serviceResult;
        } catch (IOException ex) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image");
            return null;
        }
    }

    @Override
    public ServiceResult<Image> update(Image image) {
        return null;
    }

    public void addImageData(Image image, byte[] data) {
        try {
            final String url = IMAGE_URL + "/data/" + image.getId();
            HttpResponse response = http.post(url, data, getMediaType(image));
        } catch (IOException e) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image data");
        }
    }

    public byte[] getImageData(Image image) {
        try {
            final String url = IMAGE_URL + "/data/" + image.getId();
             return http.getByte(url);
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
