package com.example.presentpu;

public class attendanceModel {
    String Name, StudentId, DateTime, Description, imageUrl;

    public attendanceModel(){}

    public attendanceModel(String name, String id, String dttm, String desc, String imgUrl) {
        this.Name = name;
        this.StudentId = id;
        this.DateTime = dttm;
        this.Description = desc;
        this.imageUrl = imgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
