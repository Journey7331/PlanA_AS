/**
 * Copyright 2022 json.cn
 */
package com.example.plana.bean;

import java.util.ArrayList;

public class PlanBean {

    private int id;
    private int pid;
    private String title;
    private int level;
    private int totalCnt;
    private int doneCnt;
    private Boolean done;
    private Boolean isLeaf;


    public PlanBean(int id, int pid, String title, int level, int totalCnt, int doneCnt, Boolean done, Boolean isLeaf) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.level = level;
        this.totalCnt = totalCnt;
        this.doneCnt = doneCnt;
        this.done = done;
        this.isLeaf = isLeaf;
    }

    public PlanBean(int id, int pid, String title, int level) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.level = level;
        this.done = false;
        this.isLeaf = false;
    }

    public PlanBean(int id, int pid, String title, int level, Boolean isLeaf) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.level = level;
        this.done = false;
        this.isLeaf = isLeaf;
    }

    public PlanBean() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public int getDoneCnt() {
        return doneCnt;
    }

    public void setDoneCnt(int doneCnt) {
        this.doneCnt = doneCnt;
    }

    public Boolean isDone() {
        return done;
    }

    public void setISDoneOrNot(Boolean done) {
        this.done = done;
    }

    public Boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeafOrNot(Boolean leaf) {
        isLeaf = leaf;
    }
}