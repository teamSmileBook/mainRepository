package com.example.smilebook.model;

public class TokenDTO {
    private String memberId;
    private String token;

    public TokenDTO(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}