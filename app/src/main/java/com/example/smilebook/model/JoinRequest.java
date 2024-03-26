package com.example.smilebook.model;

public class JoinRequest {
    private String nickname;
    private String memberId;
    private String password;
    private String password2;
    private String eMail;
    private String phoneNumber;

    public JoinRequest(String nickname, String memberId, String password, String password2, String eMail, String phoneNumber) {
        this.nickname = nickname;
        this.memberId = memberId;
        this.password = password;
        this.password2 = password2;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {return eMail;
    }

    public void setEmail(String email) {
        this.eMail = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}