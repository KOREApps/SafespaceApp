package kore.ntnu.no.safespace.service;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType JPG
            = MediaType.parse("image/jpg");
    public static final MediaType PNG
            = MediaType.parse("image/png");
    public static final MediaType BYTES
            =  MediaType.parse("application/octet-stream");

    private OkHttpClient http;

    public HttpService() {
        this.http = HttpClientBuilder.getUnsafeOkHttpClient();
    }

    public HttpResponse get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = http.newCall(request).execute();
        HttpResponse httpResponse = new HttpResponse(response.code(), response.body().string());
        response.close();
        return httpResponse;
    }

    public HttpResponse post(String url, String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = http.newCall(request).execute();
        HttpResponse httpResponse = new HttpResponse(response.code(), response.body().string());
        response.close();
        return httpResponse;
    }

    public HttpResponse post(String url, byte[] bodyBytes, MediaType mediaType) throws IOException {
        RequestBody body = RequestBody.create(mediaType, bodyBytes);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = http.newCall(request).execute();
        HttpResponse httpResponse = new HttpResponse(response.code(), response.body().string());
        response.close();
        return httpResponse;
    }

    public HttpResponse post(String url, byte[] bodyBytes) throws IOException {
        return post(url, bodyBytes, BYTES);
    }

}
