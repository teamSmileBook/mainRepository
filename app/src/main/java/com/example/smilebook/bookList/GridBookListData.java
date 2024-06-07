package com.example.smilebook.bookList;

import java.io.Serializable;

// 도서 목록의 데이터 모델을 정의. 주로 객체를 파일로 저장하거나 네트워크를 통해 전달하는 경우 활용한다.
// Serializable 인터페이스를 구현하여 객체를 직렬화할 수 있도록 한다. 주로 객체의 상태를 외부에서 저장하거나 전송할 때 활용된다.
public class GridBookListData implements Serializable {

    private Long bookId; // 도서 ID를 저장하는 변수
    private String coverUrl; // 도서 표지 이미지 URL을 저장하는 변수
    private String bookTitle; // 도서 제목을 저장하는 변수
    private String bookStatus; // 도서 상태를 저장하는 변수
    private boolean bookWished; // 도서를 찜 상태를 저장하는 변수

    // 생성자
    public GridBookListData(Long bookId, String coverUrl, String bookTitle, String bookStatus, boolean bookWished) {
        this.bookId = bookId;
        this.coverUrl = coverUrl;
        this.bookTitle = bookTitle;
        this.bookStatus = bookStatus;
        this.bookWished = bookWished;
    }


    // 도서 ID를 반환하는 메서드
    public Long getBookId() {
        return bookId;
    }

    // 도서 ID를 설정하는 메서드
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    // 도서 표지 이미지 URL을 반환하는 메서드
    public String getCoverUrl() {
        return coverUrl;
    }

    // 도서 표지 이미지 URL을 설정하는 메서드
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    // 도서 제목을 반환하는 메서드
    public String getTitle() {
        return bookTitle;
    }

    // 도서 제목을 설정하는 메서드
    public void setTitle(String title) {
        this.bookTitle = title;
    }

    // 도서 상태를 반환하는 메서드
    public String getStatus() {
        return bookStatus;
    }

    // 도서 상태를 설정하는 메서드
    public void setStatus(String status) {
        this.bookStatus = status;
    }

    // 도서를 찜한 상태를 반환하는 메서드
    public boolean isBookWished() {
        return bookWished;
    }

    // 도서를 찜한 상태를 설정하는 메서드
    public void setBookWished(boolean bookWished) {
        this.bookWished = bookWished;
    }
}
