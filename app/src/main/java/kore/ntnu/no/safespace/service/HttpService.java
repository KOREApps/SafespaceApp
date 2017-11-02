package kore.ntnu.no.safespace.service;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient http;

    public HttpService() {
        this.http = new OkHttpClient();
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = http.newCall(request).execute();
        return response.body().string();
    }

    public String post(String url, String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = http.newCall(request).execute();
        return response.body().string();
    }

}
