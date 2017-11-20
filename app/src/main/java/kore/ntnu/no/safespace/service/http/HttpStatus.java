package kore.ntnu.no.safespace.service.http;

/**
 * Created by robert on 11/20/17.
 */

public enum HttpStatus {
    OK, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST, SERVER_ERROR, UNKNOWN;

    public static HttpStatus getStatus(int code) {
        switch (code) {
            case 200:
                return OK;
            case 401:
                return UNAUTHORIZED;
            case 400:
                return BAD_REQUEST;
            case 404:
                return NOT_FOUND;
            case 500:
                return SERVER_ERROR;
            case 501:
                return SERVER_ERROR;
            case 503:
                return SERVER_ERROR;
            default:
                return UNKNOWN;
        }
    }
}
