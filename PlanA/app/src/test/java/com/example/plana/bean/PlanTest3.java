package com.example.plana.bean;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlanTest3 {

    @Test
    public void name() {

        List<Plan> plans = new ArrayList<>();

        Plan plan = new Plan();
        plan.setPlanId(3);
        plan.setImg_id(2);
        plan.setCurPoint(0);
//        plan.setCurPoint();
        plan.setDescription("考研数学");

        List<PlanBean> beans = getTestBeans();
        plan.setPlanBeans(beans);

        PlanBrief planBrief = new PlanBrief(plan.getPlanId(), "考研数学");
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
        beans.add(new PlanBean(i++, -1, "考研数学（一）", 0));
        // 0.1
        beans.add(new PlanBean(i++, 0, "一 函数", 1));
        beans.add(new PlanBean(i++, 0, "二 极限", 1));
        beans.add(new PlanBean(i++, 0, "三 连续", 1));

        // 2.1
        beans.add(new PlanBean(i++, 1, "1 性质", 2));
        beans.add(new PlanBean(i++, 1, "2 初等函数", 2));
        beans.add(new PlanBean(i++, 1, "3 常见形式", 2));

        beans.add(new PlanBean(i++, 2, "1 概念", 2));
        beans.add(new PlanBean(i++, 2, "2 性质", 2));
        beans.add(new PlanBean(i++, 2, "3 计算", 2));


        beans.add(new PlanBean(i++, 3, "1 连续", 2));
        beans.add(new PlanBean(i++, 3, "2 间断", 2));
        beans.add(new PlanBean(i++, 3, "3 性质", 2));

        beans.add(new PlanBean(i++, 4, "1 有界性", 3, true));
        beans.add(new PlanBean(i++, 4, "2 单调性", 3, true));
        beans.add(new PlanBean(i++, 4, "3 奇偶性", 3, true));
        beans.add(new PlanBean(i++, 4, "4 周期性", 3, true));

        beans.add(new PlanBean(i++, 5, "1 基本初等函数", 3, true));
        beans.add(new PlanBean(i++, 5, "2 复合函数", 3, true));

        beans.add(new PlanBean(i++, 6, "1 分段函数", 3, true));
        beans.add(new PlanBean(i++, 6, "2 反函数", 3, true));
        beans.add(new PlanBean(i++, 6, "3 隐函数", 3, true));


        beans.add(new PlanBean(i++, 7, "1 定义", 3, true));
        beans.add(new PlanBean(i++, 7, "2 充要条件", 3, true));
        beans.add(new PlanBean(i++, 7, "3 特殊极限", 3, true));

        beans.add(new PlanBean(i++, 8, "1 唯一性", 3, true));
        beans.add(new PlanBean(i++, 8, "2 有界性", 3, true));
        beans.add(new PlanBean(i++, 8, "3 局部保号性", 3, true));

        beans.add(new PlanBean(i++, 9, "1 运算法则", 3, true));
        beans.add(new PlanBean(i++, 9, "2 极限的计算步骤", 3, true));
        beans.add(new PlanBean(i++, 9, "3 单调有界规则", 3, true));
        beans.add(new PlanBean(i++, 9, "4 夹逼准则", 3, true));


        beans.add(new PlanBean(i++, 10, "1 概念", 3, true));
        beans.add(new PlanBean(i++, 10, "2 运算", 3, true));

        beans.add(new PlanBean(i++, 11, "1 概念", 3, true));
        beans.add(new PlanBean(i++, 11, "2 寻找特殊点", 3, true));
        beans.add(new PlanBean(i++, 11, "3 特殊点的类型", 3, true));

        beans.add(new PlanBean(i++, 12, "1 最值定理", 3, true));
        beans.add(new PlanBean(i++, 12, "2 有界性定理", 3, true));
        beans.add(new PlanBean(i++, 12, "3 介值定理", 3, true));
        beans.add(new PlanBean(i++, 12, "4 零点定理", 3, true));

        return beans;

    }
}