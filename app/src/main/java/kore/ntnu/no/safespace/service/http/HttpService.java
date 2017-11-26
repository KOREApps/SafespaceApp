package kore.ntnu.no.safespace.service.http;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import kore.ntnu.no.safespace.data.UserCredentials;
import kore.ntnu.no.safespace.utils.ApplicationContext;
import kore.ntnu.no.safespace.utils.IdUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class description..
 *
 * @author
 */
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
    private UserCredentials credentials;

    public HttpService() {
        this.http = HttpClientBuilder.getUnsafeOkHttpClient();
        this.credentials = getCredentials();
    }

    private UserCredentials getCredentials() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getContext());
        final String username = prefs.getString(IdUtils.USERNAME, "");
        final String password = prefs.getString(IdUtils.PASSWORD, "");
        return new UserCredentials(username, password);
    }

    private String getCredentialsHeader(UserCredentials credentials) {
        if (credentials.getUsername().equals("")) {
            return "";
        } else {
            return "Basic " + HttpBasicService.getBasicCredentials(credentials);
        }
    }

    public HttpResponse get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", getCredentialsHeader(credentials))
                .build();
        Response response = http.newCall(request).execute();
        HttpResponse httpResponse = new HttpResponse(response.code(), response.body().string());
        response.close();
        return httpResponse;
    }

    public byte[] getByte(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", getCredentialsHeader(credentials))
                .build();
        Response response = http.newCall(request).execute();
        byte[] b = response.body().bytes();
        response.close();
        return b;
    }

    public HttpResponse post(String url, String bodyString) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", getCredentialsHeader(credentials))
                .build();
        Response response = http.newCall(request).execute();
        HttpResponse httpResponse = new HttpResponse(response.code(), response.body().string());
        response.close();
        return httpResponse;
    }

    public HttpResponse postNoAuth(String url, String bodyString) throws IOException {
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
                .addHeader("Authorization", getCredentialsHeader(credentials))
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
