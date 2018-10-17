package com.pherodev.killddl.models;

import java.util.ArrayList;

public class TaskList {

    private int id;
    private String title;
    public ArrayList<Task> list; // is this necessary?

    public TaskList() {
        list = new ArrayList<>();
    }

    public TaskList(int id, String title) {
        this.id = id;
        this.title = title;
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

}
