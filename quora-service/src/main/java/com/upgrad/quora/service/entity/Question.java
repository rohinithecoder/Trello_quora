package com.upgrad.quora.service.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uuid;

    private String content;

    @GeneratedValue
    private Date date;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private Users userId;


    public String getUuid() {
        return uuid;
    }


    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
