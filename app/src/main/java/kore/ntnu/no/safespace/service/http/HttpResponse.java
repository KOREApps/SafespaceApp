package kore.ntnu.no.safespace.service.http;

/**
 * Created by Robert on 11-Nov-17.
 */

public class HttpResponse {

    private int code;
    private String response;
    private String message;
    private HttpStatus httpStatus;

    public HttpResponse(int code, String response, String message) {
        this.code = code;
        this.response = response;
        if (message == null || message.equals("")) {
            this.message = getDefaultMessage(code);
        } else {
            this.message = message;
        }
        this.httpStatus = HttpStatus.getStatus(code);
    }

    private String getDefaultMessage(int code){
        return HttpStatus.getStatus(code).toString();
    }

    public HttpResponse(int code, String response) {
        this(code, response, "");
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
