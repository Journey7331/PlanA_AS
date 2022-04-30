package com.example.plana.bean;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlanTest2 {

    @Test
    public void name() {

        List<Plan> plans = new ArrayList<>();

        Plan plan = new Plan();
        plan.setPlanId(2);
        plan.setImg_id(1);
        plan.setCurPoint(0);
//        plan.setCurPoint();
        plan.setDescription("Redis");

        List<PlanBean> beans = getTestBeans();
        plan.setPlanBeans(beans);

        PlanBrief planBrief = new PlanBrief(plan.getPlanId(), "Redis学习路径");
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
        beans.add(new PlanBean(i++, -1, "Redis", 0));
        // 0.1
        beans.add(new PlanBean(i++, 0, "数据结构和缓存", 1));
        beans.add(new PlanBean(i++, 0, "高可靠、高性能", 1));
        beans.add(new PlanBean(i++, 0, "Redis 底层实现原理", 1));
        // 2.1
        beans.add(new PlanBean(i++, 1, "1 常见数据类型", 2));
        beans.add(new PlanBean(i++, 1, "2 扩展数据类型", 2));
        beans.add(new PlanBean(i++, 1, "3 缓存", 2));

        // 2.2
        beans.add(new PlanBean(i++, 2, "1 持久化机制", 2));
        beans.add(new PlanBean(i++, 2, "2 主从复制机制", 2, true));
        beans.add(new PlanBean(i++, 2, "3 哨兵机制", 2,true));
        beans.add(new PlanBean(i++, 2, "4 故障自动恢复", 2,true));
        beans.add(new PlanBean(i++, 2, "5 切片集群", 2,true));

        // 2.1.1
        beans.add(new PlanBean(i++, 3, "1 分布式", 2));
        beans.add(new PlanBean(i++, 3, "2 哨兵集群", 2, true));

        // 2.1.2
        beans.add(new PlanBean(i++, 4, "1 String", 3, true));
        beans.add(new PlanBean(i++, 4, "2 List", 3, true));
        beans.add(new PlanBean(i++, 4, "3 Hash", 3, true));
        beans.add(new PlanBean(i++, 4, "4 Set", 3, true));
        beans.add(new PlanBean(i++, 4, "5 Sorted Set", 3, true));

        beans.add(new PlanBean(i++, 5, "1 HyperLongLog", 3, true));
        beans.add(new PlanBean(i++, 5, "2 Bitmap", 3, true));
        beans.add(new PlanBean(i++, 5, "3 GEO", 3, true));

        beans.add(new PlanBean(i++, 6, "1 缓存击穿", 3, true));
        beans.add(new PlanBean(i++, 6, "2 缓存穿透", 3, true));
        beans.add(new PlanBean(i++, 6, "3 缓存雪崩", 3, true));

        beans.add(new PlanBean(i++, 7, "1 RDB", 3, true));
        beans.add(new PlanBean(i++, 7, "2 AOF", 3, true));
        beans.add(new PlanBean(i++, 7, "3 混合持久化", 3, true));

        beans.add(new PlanBean(i++, 12, "1 CAP", 3, true));
        beans.add(new PlanBean(i++, 12, "2 分布式锁", 3, true));
        beans.add(new PlanBean(i++, 12, "3 info指令", 3, true));
        beans.add(new PlanBean(i++, 12, "4 主从复制", 3, true));
        beans.add(new PlanBean(i++, 12, "5 哨兵", 3, true));
        beans.add(new PlanBean(i++, 12, "6 集群", 3, true));

        return beans;

    }
}