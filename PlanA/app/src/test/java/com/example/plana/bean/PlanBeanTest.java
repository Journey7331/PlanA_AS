package com.example.plana.bean;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class PlanBeanTest {

    @Test
    public void name() {
        ArrayList<PlanBean> list = new ArrayList<>();
        list.add(new PlanBean(0, -1, "JavaSE", 0));

        list.add(new PlanBean(1, 0, "Java 基础语法篇", 1));
        list.add(new PlanBean(2, 0, "Java 高级语法篇", 1));

        list.add(new PlanBean(3, 1, "语言概述", 2));
        list.add(new PlanBean(4, 1, "Java 基本语法", 2));
        list.add(new PlanBean(5, 1, "Java 数组", 2));
        list.add(new PlanBean(6, 1, "面向对象（上）", 2));
        list.add(new PlanBean(7, 1, "面向对象（中）", 2));
        list.add(new PlanBean(8, 1, "面向对象（下）", 2));

//        list.add(new PlanBean(9, 8, "关键字 static", 3,2, true));
//        list.add(new PlanBean(10, 8, "抽象类与抽象方法", 3, 1, true));
//        list.add(new PlanBean(11, 8, "接口", 3, 2, true));



    }
}