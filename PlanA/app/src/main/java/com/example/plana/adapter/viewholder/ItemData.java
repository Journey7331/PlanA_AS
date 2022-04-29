package com.example.plana.adapter.viewholder;

import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class ItemData {

    public static final int ITEM_TYPE_PARENT = 0;
    public static final int ITEM_TYPE_CHILD = 1;

    private int id;
    private int pid;

    private int type;           // 显示类型
    private String text;        // 文字
    private int treeDepth = 0;      // 路径的深度
    private Boolean done = false;
    private List<ItemData> children = null;

    private boolean expand;     // 是否展开

    public ItemData(int id, int pid, String text, int treeDepth, Boolean done) {
        this.id = id;
        this.pid = pid;
        this.text = text;
        this.treeDepth = treeDepth;
        this.done = done;
    }

    public ItemData(int type, String text, int treeDepth, List<ItemData> children) {
        this.type = type;
        this.text = text;
        this.treeDepth = treeDepth;
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public List<ItemData> getChildren() {
        return children;
    }

    public void setChildren(List<ItemData> children) {
        this.children = children;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
