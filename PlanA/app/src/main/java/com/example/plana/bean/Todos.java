package com.example.plana.bean;

/**
 * @program: PlanA
 * @description: Todos Todos 类
 */
public class Todos implements Comparable {

    private int id;
    private int owner_id;
    private String content;
    private String memo;
    private boolean done;
    private String date;
    private String time;
    private float level;
    // TODO Add type
//    private String type;

    public Todos() {
        this.id = 0;
        this.content = "";
        this.done = false;
        this.date = "";
    }

    public Todos(int id, String content, boolean done, String date) {
        this.id = id;
        this.content = content;
        this.done = done;
        this.date = date;
    }

    public Todos(int id, String content, String memo, boolean done, String date, String time, float level) {
        this.id = id;
        this.content = content;
        this.memo = memo;
        this.done = done;
        this.date = date;
        this.time = time;
        this.level = level;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    @Override
    public int compareTo(Object o) {
        int id = ((Todos) o).getId();
        // 按插入的顺序排序
        return this.getId() - id;
    }

    @Override
    public String toString() {
        return "Todos{" +
                "_id=" + id +
                ", content='" + content + '\'' +
                ", memo='" + memo + '\'' +
                ", done=" + done +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", level=" + level +
                '}';
    }
}
