package com.example.plana.bean;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.plana.base.MainApplication;
import com.example.plana.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlanTest {

    @Test
    public void name() {

        List<Plan> plans = new ArrayList<>();

        Plan plan = new Plan();
        plan.setPlanId(1);
        plan.setImg_id(3);
        plan.setCurPoint(37);
//        plan.setCurPoint();
        plan.setDescription("JavaSE入门");

        List<PlanBean> beans = getTestBeans();
        plan.setPlanBeans(beans);

        PlanBrief planBrief = new PlanBrief(plan.getPlanId(), "JavaSE");
        planBrief.setTotal(getTotalLeaf(beans));
        planBrief.setDone(getDoneLeafByCurPoint(beans, plan.getCurPoint()));
        plan.setPlanBrief(planBrief);

        plans.add(plan);

        Gson gson = new Gson();
        String str_planJSON = gson.toJson(plans);
        System.out.println(str_planJSON);

    }

    private int getTotalLeaf(List<PlanBean> beans) {
        int cnt = 0;
        for (PlanBean bean : beans) {
            if (bean.isLeaf()) cnt++;
        }
        return cnt;
    }


    private int getDoneLeafByCurPoint(List<PlanBean> beans, int curPoint) {
        int cnt = 0;
        for (PlanBean bean : beans) {
            if (bean.getId() > curPoint) break;
            if (bean.isLeaf()) cnt++;
        }
        return cnt;
    }


    private List<PlanBean> getTestBeans() {
        int i = 0;
        ArrayList<PlanBean> beans = new ArrayList<>();
        // 0
        beans.add(new PlanBean(i++, -1, "JavaSE", 0));
        // 0.1
        beans.add(new PlanBean(i++, 0, "Java 基础语法篇", 1));
        beans.add(new PlanBean(i++, 0, "Java 高级语法篇", 1));
        // 2.1
        beans.add(new PlanBean(i++, 1, "1 语言概述", 2));
        beans.add(new PlanBean(i++, 1, "2 Java 基本语法", 2));
        beans.add(new PlanBean(i++, 1, "3 Java 数组", 2));
        beans.add(new PlanBean(i++, 1, "4 面向对象（上）", 2));
        beans.add(new PlanBean(i++, 1, "5 面向对象（中）", 2));
        beans.add(new PlanBean(i++, 1, "6 面向对象（下）", 2));
        // 2.2
        beans.add(new PlanBean(i++, 2, "1 异常处理", 2));
        beans.add(new PlanBean(i++, 2, "2 多线程", 2));
        beans.add(new PlanBean(i++, 2, "3 常用类", 2));
        beans.add(new PlanBean(i++, 2, "4 枚举类与注解的使用", 2));
        beans.add(new PlanBean(i++, 2, "5 集合", 2));
        beans.add(new PlanBean(i++, 2, "6 泛型", 2));
        beans.add(new PlanBean(i++, 2, "7 IO流", 2));
        beans.add(new PlanBean(i++, 2, "8 网络编程", 2));
        beans.add(new PlanBean(i++, 2, "9 反射机制", 2));

        // 2.1.1
        beans.add(new PlanBean(i++, 3, "1 背景介绍", 3, true));
        beans.add(new PlanBean(i++, 3, "2 java的特性", 3, true));
        beans.add(new PlanBean(i++, 3, "3 环境变量的配置", 3, true));
        beans.add(new PlanBean(i++, 3, "4 第一个 HelloWorld", 3, true));
        // 2.1.2
        beans.add(new PlanBean(i++, 4, "1 关键字与保留字", 3, true));
        beans.add(new PlanBean(i++, 4, "2 标识符", 3, true));
        beans.add(new PlanBean(i++, 4, "3 变量", 3, true));
        beans.add(new PlanBean(i++, 4, "4 编码", 3, true));
        beans.add(new PlanBean(i++, 4, "5 字符串", 3, true));
        beans.add(new PlanBean(i++, 4, "6 进制", 3, true));
        beans.add(new PlanBean(i++, 4, "7 运算符", 3, true));
        beans.add(new PlanBean(i++, 4, "8 程序流程控制", 3, true));

        beans.add(new PlanBean(i++, 5, "1 数组的常见概念", 3, true));
        beans.add(new PlanBean(i++, 5, "2 一维数组", 3, true));
        beans.add(new PlanBean(i++, 5, "3 多维数组", 3, true));
        beans.add(new PlanBean(i++, 5, "4 排序简述", 3, true));
        beans.add(new PlanBean(i++, 5, "5 Arrays工具类", 3, true));

        beans.add(new PlanBean(i++, 6, "1 面向对象概述", 3, true));
        beans.add(new PlanBean(i++, 6, "2 类和对象", 3, true));
        beans.add(new PlanBean(i++, 6, "3 对象的创建和使用", 3, true));
        beans.add(new PlanBean(i++, 6, "4 属性", 3, true));
        beans.add(new PlanBean(i++, 6, "5 方法", 3, true));
        beans.add(new PlanBean(i++, 6, "6 封装", 3, true));
        beans.add(new PlanBean(i++, 6, "7 构造器", 3, true));

        beans.add(new PlanBean(i++, 7, "1 继承", 3, true));
        beans.add(new PlanBean(i++, 7, "2 方法的重写", 3, true));
        beans.add(new PlanBean(i++, 7, "3 权限修饰符", 3, true));
        beans.add(new PlanBean(i++, 7, "4 实例化", 3, true));
        beans.add(new PlanBean(i++, 7, "5 多态", 3, true));
        beans.add(new PlanBean(i++, 7, "6 Object类", 3, true));

        beans.add(new PlanBean(i++, 8, "1 抽象类与抽象方法", 3, true));
        beans.add(new PlanBean(i++, 8, "2 接口", 3, true));
        beans.add(new PlanBean(i++, 8, "3 内部类", 3, true));

        beans.add(new PlanBean(i++, 9, "1 常见异常", 3, true));
        beans.add(new PlanBean(i++, 9, "2 try-catch-finally", 3, true));
        beans.add(new PlanBean(i++, 9, "3 throws", 3, true));
        beans.add(new PlanBean(i++, 9, "4 手动抛出异常：throw", 3, true));

        beans.add(new PlanBean(i++, 10, "1 基本概念：程序、进程、线程", 3, true));
        beans.add(new PlanBean(i++, 10, "2 线程的创建和使用", 3, true));
        beans.add(new PlanBean(i++, 10, "3 线程的生命周期", 3, true));
        beans.add(new PlanBean(i++, 10, "4 线程的同步", 3, true));
        beans.add(new PlanBean(i++, 10, "5 线程的通信", 3, true));

        beans.add(new PlanBean(i++, 11, "1 字符串相关类", 3, true));
        beans.add(new PlanBean(i++, 11, "2 日期时间API", 3, true));
        beans.add(new PlanBean(i++, 11, "3 Java比较器", 3, true));
        beans.add(new PlanBean(i++, 11, "4 System类", 3, true));

        beans.add(new PlanBean(i++, 12, "1 枚举的使用", 3, true));
        beans.add(new PlanBean(i++, 12, "2 注解的使用", 3, true));

        beans.add(new PlanBean(i++, 13, "1 Collection接口", 3, true));
        beans.add(new PlanBean(i++, 13, "2 Iterator迭代器接口", 3, true));
        beans.add(new PlanBean(i++, 13, "3 List接口", 3, true));
        beans.add(new PlanBean(i++, 13, "4 Set接口", 3, true));
        beans.add(new PlanBean(i++, 13, "5 Map接口", 3, true));
        beans.add(new PlanBean(i++, 13, "6 Collections工具类", 3, true));

        beans.add(new PlanBean(i++, 14, "1 在集合中使用泛型", 3, true));
        beans.add(new PlanBean(i++, 14, "2 自定义泛型结构", 3, true));
        beans.add(new PlanBean(i++, 14, "3 通配符的使用", 3, true));

        beans.add(new PlanBean(i++, 15, "1 File类", 3, true));
        beans.add(new PlanBean(i++, 15, "2 IO流原理", 3, true));
        beans.add(new PlanBean(i++, 15, "3 文件流", 3, true));
        beans.add(new PlanBean(i++, 15, "4 缓冲流", 3, true));
        beans.add(new PlanBean(i++, 15, "5 转换流", 3, true));
        beans.add(new PlanBean(i++, 15, "6 输入、输出流", 3, true));
        beans.add(new PlanBean(i++, 15, "7 打印流", 3, true));
        beans.add(new PlanBean(i++, 15, "8 数据流", 3, true));
        beans.add(new PlanBean(i++, 15, "9 对象流", 3, true));

        beans.add(new PlanBean(i++, 16, "1 IP和端口号", 3, true));
        beans.add(new PlanBean(i++, 16, "2 网络协议", 3, true));
        beans.add(new PlanBean(i++, 16, "3 TCP网络编程", 3, true));
        beans.add(new PlanBean(i++, 16, "4 UDP网络编程", 3, true));
        beans.add(new PlanBean(i++, 16, "5 URL编程", 3, true));

        beans.add(new PlanBean(i++, 17, "1 获取Class实例", 3, true));
        beans.add(new PlanBean(i++, 17, "2 类加载ClassLoader", 3, true));
        beans.add(new PlanBean(i++, 17, "3 动态代理", 3, true));

        return beans;

    }
}