package com.example.smilebook.model;

public class BookLocationDTO {
    private String floor;
    private float xCoordinate;
    private float yCoordinate;
    private String category;

    private Long bookId;

    public BookLocationDTO() {
    }

    public BookLocationDTO(String floor, float xCoordinate, float yCoordinate, String category, Long bookId) {
        this.floor = floor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.category = category;
        this.bookId = bookId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
