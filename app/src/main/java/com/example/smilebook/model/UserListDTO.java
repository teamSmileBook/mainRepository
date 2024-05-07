package com.example.smilebook.model;

public class UserListDTO {
    private String memberId;
    private String nickname;
    private String memberStatus;

    public UserListDTO(String memberId, String nickname, String memberStatus) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.memberStatus = memberStatus;
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

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }
}
