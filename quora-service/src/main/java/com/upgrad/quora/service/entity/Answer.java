package com.upgrad.quora.service.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uuid;

    private String ans;

//    @GeneratedValue
    private LocalDateTime date;

    @ManyToOne()
    @JoinColumn(name="question_id")
    private Question questionId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users userId;

    public int getId() { return id; }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getAns() { return ans; }

    public void setAns(String ans) { this.ans = ans; }

    public LocalDateTime getDate() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public Question getQuestionId() { return questionId; }

    public void setQuestionId(Question questionId) { this.questionId = questionId; }

    public Users getUserId() { return userId; }

    public void setUserId(Users userId) { this.userId = userId; }

}
