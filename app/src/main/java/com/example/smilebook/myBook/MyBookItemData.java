package com.example.smilebook.myBook;

public class MyBookItemData {
    private Long bookId;
    private String coverUrl;
    private String bookTitle;
    private String bookStatus;


    public MyBookItemData(Long bookId, String coverUrl, String bookTitle, String bookStatus) {
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
}
