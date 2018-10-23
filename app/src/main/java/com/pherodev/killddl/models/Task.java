package com.pherodev.killddl.models;

import java.util.Date;

public class Task {

    private int id;
    private int categoryId;
    private String title;
    private String description;
    private Date deadline;

    public Task (int id, int categoryId, String title, String description, Date deadline) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

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
}
