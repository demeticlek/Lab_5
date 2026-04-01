package com.example.lab_4;

public class TodoItem {

    private long id;
    private String todo;
    private boolean urgent;

    public TodoItem(long id, String todo, boolean urgent) {
        this.id = id;
        this.todo = todo;
        this.urgent = urgent;
    }

    public long getId() { return id; }
    public String getTodo() { return todo; }
    public boolean isUrgent() { return urgent; }
}