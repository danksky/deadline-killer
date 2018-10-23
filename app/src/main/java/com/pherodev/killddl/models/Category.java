package com.pherodev.killddl.models;

import java.util.ArrayList;
// TODO - probably actually want to call this category TBH LOL
public class Category {

    private long id;
    private String title;
    public ArrayList<Task> list; // is this necessary?

    public Category() {
        list = new ArrayList<>();
    }

    public Category(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
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
