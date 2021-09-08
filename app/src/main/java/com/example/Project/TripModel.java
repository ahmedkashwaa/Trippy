package com.example.Project;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TripModel implements Serializable {
    private String date ,time;
    private String title,startPoint,endPoint;
    private int id;
    String status;
    String notes;

    public TripModel() {
    }

    public TripModel(int id,String date, String time, String title, String startPoint, String endPoint,String status) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.id = id;
        this.status = status;
    }
    public TripModel(int id,String date, String time, String title, String startPoint, String endPoint,String status,String notes) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.id = id;
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }


}
