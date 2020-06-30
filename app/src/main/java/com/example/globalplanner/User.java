package com.example.globalplanner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class User {
    private int id;
    private String email;
    private String jwt;
    private String message;
    private Data data;

    public User(String email, String jwt) {
        this.email = email;
        this.jwt = jwt;
    }
    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public void sentEmail(String email) {
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setJwt(){
        this.jwt = data.getJwt();
    }
}
