package com.example.smilebook.model;

import java.util.List;

public class WishlistDTO {

    private Long bookId;
    private String memberId;
    private List<Long> wishlist;


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
    public List<Long> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Long> wishlist) {
        this.wishlist = wishlist;
    }

}
