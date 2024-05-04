package com.example.smilebook.model;

public class ReservationDTO {

    private String memberId;

    private Long bookId;

    public ReservationDTO() {
    }

    public ReservationDTO(String memberId, Long bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
