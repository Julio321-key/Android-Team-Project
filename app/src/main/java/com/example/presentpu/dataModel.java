package com.example.presentpu;

public class dataModel {
    private String fullName;
    private String studentID;
    private String dateTime;
    private String description;

    private String PhotoUrl;


    // Konstruktor
    public dataModel(String fullName, String studentID, String dateTime, String description, String PhotoUrl) {
        this.fullName = fullName;
        this.studentID = studentID;
        this.dateTime = dateTime;
        this.description = description;
        this.PhotoUrl = PhotoUrl;
    }

    // Getter dan Setter
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

