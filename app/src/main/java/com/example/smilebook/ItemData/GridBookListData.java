package com.example.smilebook.ItemData;

public class GridBookListData {

    private Long bookId;
    private String coverUrl;
    private String bookTitle;
    private String bookStatus;

    public GridBookListData(Long bookId, String coverUrl, String bookTitle, String bookStatus) {
        this.bookId = bookId;
        this.coverUrl = coverUrl;
        this.bookTitle = bookTitle;
        this.bookStatus = bookStatus;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return bookTitle;
    }

    public void setTitle(String title) {
        this.bookTitle = title;
    }

    public String getStatus() {
        return bookStatus;
    }

    public void setStatus(String status) {
        this.bookStatus = status;
    }
}
