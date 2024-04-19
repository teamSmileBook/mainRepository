package com.example.smilebook.model;

public class BookLocationDTO {
    private String floor;
    private float xcoordinate;
    private float ycoordinate;
    private String category;

    private Long bookId;

    public BookLocationDTO() {
    }

    public BookLocationDTO(String floor, float xcoordinate, float ycoordinate, String category, Long bookId) {
        this.floor = floor;
        this.xcoordinate = xcoordinate;
        this.ycoordinate = ycoordinate;
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
        return xcoordinate;
    }

    public void setXCoordinate(float xCoordinate) {
        this.xcoordinate = xCoordinate;
    }

    public float getYCoordinate() {
        return ycoordinate;
    }

    public void setYCoordinate(float yCoordinate) {
        this.ycoordinate = yCoordinate;
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
