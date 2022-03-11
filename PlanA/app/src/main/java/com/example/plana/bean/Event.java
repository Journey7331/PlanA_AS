package com.example.plana.bean;

/**
 * @program: PlanA
 * @description: Event Thing 类
 */
public class Event implements Comparable {

    private int _id;
    private String content;
    private String memo;
    private boolean done;
    private String date;
    private String time;
    private float level;
    // TODO Add type
//    private String type;

    public Event() {
        this._id = 0;
        this.content = "";
        this.done = false;
        this.date = "";
    }

    public Event(int _id, String content, boolean done, String date) {
        this._id = _id;
        this.content = content;
        this.done = done;
        this.date = date;
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

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
        int id = ((Event) o).get_id();
        // 按插入的顺序排序
        return this.get_id() - id;
    }

}
