package com.example.smilebook.ItemData;

import java.io.Serializable;

//Serializable : 객체를 직렬화함. 객체의 상태를 외부에서 저장하거나 전송할 수 있는 방법을 제공하는 인터페이스
//주로 객체를 파일로 저장하거나 네트워크를 통해 전달하는 경우 활용
public class GridBookListData implements Serializable {

    private Long bookId;
    private String coverUrl;
    private String bookTitle;
    private String bookStatus;
    private boolean bookWished;

    public GridBookListData(Long bookId, String coverUrl, String bookTitle, String bookStatus, boolean bookWished) {
        this.bookId = bookId;
        this.coverUrl = coverUrl;
        this.bookTitle = bookTitle;
        this.bookStatus = bookStatus;
        this.bookWished = bookWished;
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

    public boolean isBookWished() {
        return bookWished;
    }

    public void setBookWished(boolean bookWished) {
        this.bookWished = bookWished;
    }
}
