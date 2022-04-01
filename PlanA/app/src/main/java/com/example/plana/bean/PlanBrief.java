package com.example.plana.bean;

/**
 * @program: PlanA
 * @description:
 */
public class PlanBrief {

    private String planName;
    private int total;
    private int done;
    private int leftDays;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(int leftDays) {
        this.leftDays = leftDays;
    }

    public PlanBrief(String planName, int total, int done, int leftDays) {
        this.planName = planName;
        this.total = total;
        this.done = done;
        this.leftDays = leftDays;
    }

    @Override
    public String toString() {
        return "PlanBrief{" +
                "planName='" + planName + '\'' +
                ", total=" + total +
                ", done=" + done +
                ", leftDays=" + leftDays +
                '}';
    }
}
