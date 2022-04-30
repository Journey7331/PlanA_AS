package com.example.plana.bean;

import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class Plan {

    private int planId;
    private String description;
    private int img_id;
    private int curPoint;
    private List<PlanBean> planBeans;
    private PlanBrief planBrief;


    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }

    public int getCurPoint() {
        return curPoint;
    }

    public void setCurPoint(int curPoint) {
        this.curPoint = curPoint;
    }

    public List<PlanBean> getPlanBeans() {
        return planBeans;
    }

    public void setPlanBeans(List<PlanBean> planBeans) {
        this.planBeans = planBeans;
    }

    public PlanBrief getPlanBrief() {
        return planBrief;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planId=" + planId +
                ", description='" + description + '\'' +
                ", img_id=" + img_id +
                ", curPoint=" + curPoint +
                ", planBeans=" + planBeans +
                ", planBrief=" + planBrief +
                '}';
    }

    public void setPlanBrief(PlanBrief planBrief) {
        this.planBrief = planBrief;
    }
}
