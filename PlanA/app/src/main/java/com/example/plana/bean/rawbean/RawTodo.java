/**
 * Copyright 2022 json.cn
 */
package com.example.plana.bean.rawbean;

public class RawTodo {

    private int id;
    private int owner_id;
    private String content;
    private String memo;
    private String due_date;
    private String due_time;
    private float importance;
    private int done;

    public RawTodo() {
    }

    public RawTodo(int id, int owner_id, String content, String memo, String due_date, String due_time, float importance, int done) {
        this.id = id;
        this.owner_id = owner_id;
        this.content = content;
        this.memo = memo;
        this.due_date = due_date;
        this.due_time = due_time;
        this.importance = importance;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public float getImportance() {
        return importance;
    }

    public void setImportance(float importance) {
        this.importance = importance;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "RawTodo{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", content='" + content + '\'' +
                ", memo='" + memo + '\'' +
                ", due_date='" + due_date + '\'' +
                ", due_time='" + due_time + '\'' +
                ", importance=" + importance +
                ", done=" + done +
                '}';
    }
}