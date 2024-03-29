package com.example.smilebook.model;

public class MemberDTO {
    private String memberId;
    private String nickname;
    private String password;
    private String password2;
    private String email;
    private String phoneNumber;


    public MemberDTO() {
    }

    public MemberDTO(String memberId, String nickname, String password, String password2, String email, String phoneNumber) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.password = password;
        this.password2 = password2;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

