package com.upgrad.quora.api.requestresponse;

public class AnswerDeleteResponse {
    private String id;
    private String status;

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

    public AnswerDeleteResponse(String id, String status) {
        this.id = id;
        this.status = status;
    }
}
