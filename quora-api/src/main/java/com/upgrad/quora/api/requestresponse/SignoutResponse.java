package com.upgrad.quora.api.requestresponse;

public class SignoutResponse {
    private String id;

    public String getId() {
        return id;
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

    private String message;

    public SignoutResponse(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
