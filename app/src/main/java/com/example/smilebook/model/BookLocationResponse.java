package com.example.smilebook.model;

public class BookLocationResponse {
    private int floor;
    private int xCoordinate;
    private int yCoordinate;
    private String category;

    public BookLocationResponse() {
    }

    public BookLocationResponse(int floor, int xCoordinate, int yCoordinate, String category) {
        this.floor = floor;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.category = category;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
