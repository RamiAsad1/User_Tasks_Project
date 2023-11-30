package com.example.user_tasks_project;

import java.util.ArrayList;

public class Task {
    private int Id;
    private String title;
    private String description;
    private boolean finished = false;
    private static int nextId = 1;//This counter is used to auto increment the IDs of tasks
    public static ArrayList<Task> tasks = new ArrayList<>();

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Task(String title, String description, boolean finished) {
        Id = nextId;
        this.title = title;
        this.description = description;
        this.finished = finished;
        nextId++;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "title: " + title ;
    }

    public static void addTask(Task t){
            tasks.add(t);
    }
    public static void removeTask(int taskID){
        for (Task t: tasks){
            if(t.getId() == taskID){
                tasks.remove(t);
                break;
            }
        }
    }
}
