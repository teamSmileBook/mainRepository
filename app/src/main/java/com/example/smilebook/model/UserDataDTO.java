package com.example.smilebook.model;

public class UserDataDTO {

    private String userId;
    private String rfidcardId;
    private String memberStatus;
    private Integer warningCount;

    public UserDataDTO() {
    }

    public UserDataDTO(String userId, String rfidcardId, String memberStatus, Integer warningCount) {
        this.userId = userId;
        this.rfidcardId = rfidcardId;
        this.memberStatus = memberStatus;
        this.warningCount = warningCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRfidCardId() {
        return rfidcardId;
    }

    public void setRfidCardId(String rfidCardId) {
        this.rfidcardId = rfidCardId;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }
}
