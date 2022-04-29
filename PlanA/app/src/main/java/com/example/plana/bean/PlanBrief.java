package com.example.plana.bean;

/**
 * @program: PlanA
 * @description:
 */
public class PlanBrief {

    private int planId;
    private String planName;
    private int total;
    private int done;

//    private int leftDays;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

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

    public PlanBrief(int planId, String planName) {
        this.planId = planId;
        this.planName = planName;
    }

    public PlanBrief(int id, String planName, int total, int done) {
        this.planId = id;
        this.planName = planName;
        this.total = total;
        this.done = done;
    }

    @Override
    public String toString() {
        return "PlanBrief{" +
                "planName='" + planName + '\'' +
                ", total=" + total +
                ", done=" + done +
                '}';
    }
}
