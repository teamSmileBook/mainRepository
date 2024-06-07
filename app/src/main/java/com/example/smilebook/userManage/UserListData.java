package com.example.smilebook.userManage;

import java.io.Serializable;

// 회원 목록 RecyclerView 아이템에 표시될 데이터들을 나타냄.
public class UserListData implements Serializable {

    private String memberId;
    private String nickname;
    private String memberStatus;

    public UserListData(String memberId, String nickname, String memberStatus){
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
