package com.example.smilebook.model;

public class SuspensionReasonDTO {
    private String memberId;
    private String suspensionReason;

    // 생성자
    public SuspensionReasonDTO(String memberId, String suspensionReason) {
        this.memberId = memberId;
        this.suspensionReason = suspensionReason;
    }

    public SuspensionReasonDTO() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }
}
