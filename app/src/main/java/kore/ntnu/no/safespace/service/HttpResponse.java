package kore.ntnu.no.safespace.service;

/**
 * Created by Robert on 11-Nov-17.
 */

public class HttpResponse {

    private int code;
    private String response;

    public HttpResponse(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
