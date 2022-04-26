package com.example.plana.bean;

import com.example.plana.utils.TimeCalcUtil;

/**
 * @program: PlanA
 * @description:
 */
public class TimerRecorder {

    private Integer _id;
    private String day;
    private String startTime;
    private String endTime;
    private Integer time;
    private String count_type;
    private String tag;

    public static final String POSITIVE = "POSITIVE";
    public static final String NEGATIVE = "NEGATIVE";

    public TimerRecorder() {
    }

    public TimerRecorder(String day, int startHour, int startMinute, int time, String count_type) {
        this.day = day;
        this.startTime = TimeCalcUtil.format02d(startHour, startMinute);
        this.endTime = TimeCalcUtil.calcEndTime(startHour, startMinute, time);
        this.time = time;
        this.count_type = count_type;
        this.tag = null;
    }

    public TimerRecorder(int _id, String day, String startTime, String endTime, int time, String count_type,  String tag) {
        this._id = _id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.time = time;
        this.count_type = count_type;
        this.tag = tag;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getCount_type() {
        return count_type;
    }

    public void setCount_type(String count_type) {
        this.count_type = count_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TimerRecorder{" +
                "_id=" + _id +
                ", day='" + day + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", time=" + time +
                ", count_type='" + count_type + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
