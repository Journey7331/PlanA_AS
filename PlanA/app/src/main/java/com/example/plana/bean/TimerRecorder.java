package com.example.plana.bean;

import com.example.plana.utils.TimeCalcUtil;

/**
 * @program: PlanA
 * @description:
 */
public class TimerRecorder {

    private String day;
    private String startTime;
    private String endTime;
    private int time;

    public TimerRecorder() {
    }

    public TimerRecorder(String day, int startHour, int startMinute, int time) {
        this.day = day;
        this.startTime = TimeCalcUtil.format02d(startHour, startMinute);
        this.endTime = TimeCalcUtil.calcEndTime(startHour, startMinute, time);
        this.time = time;
    }

    public TimerRecorder(String day, String startTime, String endTime, int time) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.time = time;
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

    @Override
    public String toString() {
        return "TimerRecorder{" +
                "day='" + day + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", time=" + time +
                '}';
    }
}
