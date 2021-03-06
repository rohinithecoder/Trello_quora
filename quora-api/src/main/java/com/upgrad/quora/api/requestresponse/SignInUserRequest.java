package com.upgrad.quora.api.requestresponse;

public class SignInUserRequest {
    private String user_name;
    private String password;

    @Override
    public String toString() {
        return "SignInUserRequest{" +
            ", user_name='" + user_name + '\'' +
            ", password='" + password + '\'' +
                '}';

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
