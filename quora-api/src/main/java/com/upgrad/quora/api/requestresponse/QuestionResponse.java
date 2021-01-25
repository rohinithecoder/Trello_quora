package com.upgrad.quora.api.requestresponse;

public class QuestionResponse {
    private String id;
    private String status;

    public QuestionResponse(String id, String message) {
        this.id = id;
        this.status = message;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
