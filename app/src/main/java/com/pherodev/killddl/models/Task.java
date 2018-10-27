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

    public Task (long id, long categoryId, String title, String description, Date deadline) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
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

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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
