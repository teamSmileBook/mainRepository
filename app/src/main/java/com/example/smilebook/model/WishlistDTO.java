package com.example.smilebook.model;

public class WishlistDTO {

    private Long bookId;
    private String memberId;

    public WishlistDTO(String memberId, Long bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

}
