package com.upgrad.quora.api.requestresponse;

public class QuestionEditResponse {
    private String id;
    private String status;

    public QuestionEditResponse(String id, String content) {
        this.id = id;
        this.status = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
