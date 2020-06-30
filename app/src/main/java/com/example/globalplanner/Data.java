package com.example.globalplanner;

public class Data {

    private String jwt;
    private Integer expires_at;

    public void setJwt(String jwt) {
         this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setExpiresAt(Integer expires_at) {
        this.expires_at = expires_at;
    }

    public Integer getExpiresAt() {
        return expires_at;
    }
}
