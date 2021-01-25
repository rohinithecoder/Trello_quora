package com.upgrad.quora.api.requestresponse;

public class AnswerDetailsResponse {
    private String id;
    private String questionContent;
    private String answerContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public AnswerDetailsResponse(String id, String questionContent, String answerContent) {
        this.id = id;
        this.questionContent = questionContent;
        this.answerContent = answerContent;
    }
}
