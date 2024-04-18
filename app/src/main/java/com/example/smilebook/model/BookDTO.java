package com.example.smilebook.model;

import androidx.annotation.StringDef;

import java.time.LocalDateTime;

public class BookDTO {

    private Long bookId;
    private String bookRFID;
    private String bookTitle;
    private String coverUrl;
    private String author;
    private String publisher;
    private String memo;
    private String description;
    private String contents;
    private String bookStatus;
    private LocalDateTime reservationTime;
    private LocalDateTime loneDate;
    private MemberDTO member;

    public BookDTO(Long bookId, String bookRFID, String bookTitle,  String coverUrl,
                   String author, String publisher, String memo, String description, String contents,
                   String bookStatus, LocalDateTime reservationTime, LocalDateTime loneDate, MemberDTO member) {

        this.bookId = bookId;
        this.bookRFID = bookRFID;
        this.bookTitle = bookTitle;
        this.coverUrl = coverUrl;
        this.author = author;
        this.publisher = publisher;
        this.memo = memo;
        this.description = description;
        this.contents = contents;
        this.bookStatus = bookStatus;
        this.reservationTime = reservationTime;
        this.loneDate = loneDate;
        this.member = member;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookRFID() {
        return bookRFID;
    }

    public void setBookRFID(String bookRFID) {
        this.bookRFID = bookRFID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getLoneDate() {
        return loneDate;
    }

    public void setLoneDate(LocalDateTime loneDate) {
        this.loneDate = loneDate;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }
}
