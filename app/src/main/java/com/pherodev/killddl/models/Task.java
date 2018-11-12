package com.pherodev.killddl.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Task implements Parcelable {

    private long id;
    private long categoryId;
    private String title;
    private String description;
    private Date deadline;

    // Sprint 1 new variables
    // color
    // isDone
    // listPosition

    private int color;
    private boolean isCompleted;
    private int priority;
    private int recurringSchedule;

    public Task (long id, long categoryId, String title, String description, Date deadline, Boolean isCompleted, int color, int priority, int recurringSchedule) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
        this.color = color;
        this.priority = priority;
        this.recurringSchedule = recurringSchedule;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        categoryId = in.readLong();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCategoryId() { return categoryId; }

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getDeadline() {
        return deadline;
    }

    public int getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getColorPosition() {
        switch (color) {
            case -65536:
                return 0; // red
            case -16776961:
                return 1;
            case -16711936:
                return 2;
            case -8388480:
                return 3;
            default:
                return 0;
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRecurringSchedule(int recurringSchedule){ this.recurringSchedule = recurringSchedule; }

    public int getRecurringSchedule(){ return this.recurringSchedule;}

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void complete(boolean isCompleted){
        this.isCompleted = isCompleted;
    }

    public boolean getIsCompleted(){
        return isCompleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(categoryId);
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
