package com.upgrad.quora.api.requestresponse;

public class SigninResponse {

    private String id;
    private String message;

    public SigninResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SigninResponse{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
