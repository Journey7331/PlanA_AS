
package com.example.plana.bean.rawbean;

public class RawSubject {

    private int id;
    private int owner_id;
    private String name;
    private String room;
    private String teacher;
    private String week_list;
    private int start;
    private int step;
    private int day;

    @Override
    public String toString() {
        return "RawSubject{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", name='" + name + '\'' +
                ", room='" + room + '\'' +
                ", teacher='" + teacher + '\'' +
                ", week_list='" + week_list + '\'' +
                ", start=" + start +
                ", step=" + step +
                ", day=" + day +
                '}';
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWeek_list() {
        return week_list;
    }

    public void setWeek_list(String week_list) {
        this.week_list = week_list;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public RawSubject(int id, int owner_id, String name, String room, String teacher, String week_list, int start, int step, int day) {
        this.id = id;
        this.owner_id = owner_id;
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.week_list = week_list;
        this.start = start;
        this.step = step;
        this.day = day;
    }

    public RawSubject() {
    }
}