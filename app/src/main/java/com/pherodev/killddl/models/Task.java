package com.pherodev.killddl.models;

import java.util.Date;

public class Task {

    private int id;
    private String title;
    private Date deadline;

    public Task (String title, Date deadline) {
        this.title = title;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
