package com.example.smilebook.model;

import java.util.List;

public class WishlistItemDTO {

    private String memberId;
    private List<Long> bookIds;

    public WishlistItemDTO() {
    }

    public WishlistItemDTO(String memberId, List<Long> bookIds) {
        this.memberId = memberId;
        this.bookIds = bookIds;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
