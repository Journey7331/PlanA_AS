package com.example.plana.function.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji2.widget.EmojiButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.PlanBriefAdapter;
import com.example.plana.base.BaseFragment;
import com.example.plana.base.MainApplication;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.bean.PlanBean;
import com.example.plana.bean.PlanBrief;
import com.example.plana.function.plan.AchievementActivity;
import com.example.plana.function.plan.AddPlanActivity;
import com.example.plana.utils.SharedPreferencesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */
public class PlanFragment extends BaseFragment {

    public static final String TAG = "PlanFragment";

    TextView tvPlanTitle;
    RecyclerView recyclerView;
    PlanBriefAdapter adapter;

    RelativeLayout rlEmpty;

    EmojiButton btAchievement;
    FloatingActionButton fab;

    // 卡片之间的间距
    int space = 30;

    // GridView 的列数
    int column = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        tvPlanTitle = view.findViewById(R.id.tv_plan_brief_list_title);
        recyclerView = view.findViewById(R.id.plans_recycler_view);
        rlEmpty = view.findViewById(R.id.rl_plan_list_empty_status);
        tvPlanTitle.append("  做个计划吧~");

        loadPlansFromSP();

        initRecyclerView();

//        view.findViewById(R.id.bt_my_achievement).setOnClickListener(l -> {
//            startActivity(new Intent(getContext(), AchievementActivity.class));
//        });

        view.findViewById(R.id.add_plan_fab).setOnClickListener(l -> {
            startActivity(new Intent(getContext(), AddPlanActivity.class));
        });

        return view;
    }


    /**
     * 从本地载入plans
     */
    private List<Plan> loadPlansFromSP() {
        if (My.plans.size() > 0) return My.plans;

        String str_planJSON = SharedPreferencesUtil
                .init(MainApplication.getAppContext(), "PLAN_DATA")
                .getString("PLAN_LIST", null);

        // default test
//        if (str_planJSON == null) {
//            str_planJSON = "[{\"planId\":1,\"description\":\"JavaSE入门\",\"img_id\":3,\"curPoint\":37,\"planBeans\":[{\"id\":0,\"pid\":-1,\"title\":\"JavaSE\",\"level\":0,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":1,\"pid\":0,\"title\":\"Java 基础语法篇\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":2,\"pid\":0,\"title\":\"Java 高级语法篇\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":3,\"pid\":1,\"title\":\"1 语言概述\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":4,\"pid\":1,\"title\":\"2 Java 基本语法\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":5,\"pid\":1,\"title\":\"3 Java 数组\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":6,\"pid\":1,\"title\":\"4 面向对象（上）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":7,\"pid\":1,\"title\":\"5 面向对象（中）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":8,\"pid\":1,\"title\":\"6 面向对象（下）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":9,\"pid\":2,\"title\":\"1 异常处理\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":10,\"pid\":2,\"title\":\"2 多线程\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":11,\"pid\":2,\"title\":\"3 常用类\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":12,\"pid\":2,\"title\":\"4 枚举类与注解的使用\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":13,\"pid\":2,\"title\":\"5 集合\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":14,\"pid\":2,\"title\":\"6 泛型\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":15,\"pid\":2,\"title\":\"7 IO流\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":16,\"pid\":2,\"title\":\"8 网络编程\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":17,\"pid\":2,\"title\":\"9 反射机制\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":18,\"pid\":3,\"title\":\"1 背景介绍\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":19,\"pid\":3,\"title\":\"2 java的特性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":20,\"pid\":3,\"title\":\"3 环境变量的配置\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":21,\"pid\":3,\"title\":\"4 第一个 HelloWorld\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":22,\"pid\":4,\"title\":\"1 关键字与保留字\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":23,\"pid\":4,\"title\":\"2 标识符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":24,\"pid\":4,\"title\":\"3 变量\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":25,\"pid\":4,\"title\":\"4 编码\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":26,\"pid\":4,\"title\":\"5 字符串\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":27,\"pid\":4,\"title\":\"6 进制\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":28,\"pid\":4,\"title\":\"7 运算符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":29,\"pid\":4,\"title\":\"8 程序流程控制\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":30,\"pid\":5,\"title\":\"1 数组的常见概念\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":31,\"pid\":5,\"title\":\"2 一维数组\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":32,\"pid\":5,\"title\":\"3 多维数组\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":33,\"pid\":5,\"title\":\"4 排序简述\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":34,\"pid\":5,\"title\":\"5 Arrays工具类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":35,\"pid\":6,\"title\":\"1 面向对象概述\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":36,\"pid\":6,\"title\":\"2 类和对象\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":37,\"pid\":6,\"title\":\"3 对象的创建和使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":38,\"pid\":6,\"title\":\"4 属性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":39,\"pid\":6,\"title\":\"5 方法\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":40,\"pid\":6,\"title\":\"6 封装\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":41,\"pid\":6,\"title\":\"7 构造器\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":42,\"pid\":7,\"title\":\"1 继承\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":43,\"pid\":7,\"title\":\"2 方法的重写\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":44,\"pid\":7,\"title\":\"3 权限修饰符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":45,\"pid\":7,\"title\":\"4 实例化\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":46,\"pid\":7,\"title\":\"5 多态\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":47,\"pid\":7,\"title\":\"6 Object类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":48,\"pid\":8,\"title\":\"1 抽象类与抽象方法\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":49,\"pid\":8,\"title\":\"2 接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":50,\"pid\":8,\"title\":\"3 内部类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":51,\"pid\":9,\"title\":\"1 常见异常\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":52,\"pid\":9,\"title\":\"2 try-catch-finally\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":53,\"pid\":9,\"title\":\"3 throws\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":54,\"pid\":9,\"title\":\"4 手动抛出异常：throw\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":55,\"pid\":10,\"title\":\"1 基本概念：程序、进程、线程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":56,\"pid\":10,\"title\":\"2 线程的创建和使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":57,\"pid\":10,\"title\":\"3 线程的生命周期\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":58,\"pid\":10,\"title\":\"4 线程的同步\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":59,\"pid\":10,\"title\":\"5 线程的通信\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":60,\"pid\":11,\"title\":\"1 字符串相关类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":61,\"pid\":11,\"title\":\"2 日期时间API\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":62,\"pid\":11,\"title\":\"3 Java比较器\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":63,\"pid\":11,\"title\":\"4 System类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":64,\"pid\":12,\"title\":\"1 枚举的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":65,\"pid\":12,\"title\":\"2 注解的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":66,\"pid\":13,\"title\":\"1 Collection接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":67,\"pid\":13,\"title\":\"2 Iterator迭代器接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":68,\"pid\":13,\"title\":\"3 List接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":69,\"pid\":13,\"title\":\"4 Set接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":70,\"pid\":13,\"title\":\"5 Map接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":71,\"pid\":13,\"title\":\"6 Collections工具类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":72,\"pid\":14,\"title\":\"1 在集合中使用泛型\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":73,\"pid\":14,\"title\":\"2 自定义泛型结构\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":74,\"pid\":14,\"title\":\"3 通配符的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":75,\"pid\":15,\"title\":\"1 File类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":76,\"pid\":15,\"title\":\"2 IO流原理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":77,\"pid\":15,\"title\":\"3 文件流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":78,\"pid\":15,\"title\":\"4 缓冲流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":79,\"pid\":15,\"title\":\"5 转换流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":80,\"pid\":15,\"title\":\"6 输入、输出流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":81,\"pid\":15,\"title\":\"7 打印流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":82,\"pid\":15,\"title\":\"8 数据流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":83,\"pid\":15,\"title\":\"9 对象流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":84,\"pid\":16,\"title\":\"1 IP和端口号\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":85,\"pid\":16,\"title\":\"2 网络协议\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":86,\"pid\":16,\"title\":\"3 TCP网络编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":87,\"pid\":16,\"title\":\"4 UDP网络编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":88,\"pid\":16,\"title\":\"5 URL编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":89,\"pid\":17,\"title\":\"1 获取Class实例\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":90,\"pid\":17,\"title\":\"2 类加载ClassLoader\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":91,\"pid\":17,\"title\":\"3 动态代理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true}],\"planBrief\":{\"planId\":1,\"planName\":\"JavaSE\",\"total\":74,\"done\":20}}]";
//        }
        if (str_planJSON == null) return new ArrayList<>();

        List<Plan> plans = new Gson().fromJson(str_planJSON, new TypeToken<List<Plan>>() {
        }.getType());

        updatePlanListSelf(plans);
        return My.plans = plans;
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePlanListSelf(My.plans);
        initRecyclerView();
    }

    public void updatePlanListSelf(List<Plan> plans) {
        for (Plan plan : plans) {
            List<PlanBean> beans = plan.getPlanBeans();
            for (PlanBean bean : beans) {
                bean.setDoneCnt(0);
                bean.setTotalCnt(0);
            }
            PlanBean bean = beans.get(0);
            loopUpdate(beans, bean, plan.getCurPoint(), 0);

            PlanBrief brief = plan.getPlanBrief();
            brief.setPlanName(bean.getTitle());
            brief.setTotal(bean.getTotalCnt());
            brief.setDone(bean.getDoneCnt());
        }
    }

    /**
     * 循环自更新当前 My.plans 中每个 plan 的列表内容
     * <p>
     * beans: 全部的beans列表
     * bean： 第一个bean
     */
    public void loopUpdate(List<PlanBean> beans, PlanBean bean, int curPoint, int level) {
        if (bean.getLevel() != level) return;

        int id = bean.getId();
        if (bean.isLeaf()) {
            bean.setTotalCnt(1);
            bean.setDoneCnt(0);
            bean.setISDoneOrNot(false);
            if (id <= curPoint) {
                bean.setISDoneOrNot(true);
                bean.setDoneCnt(1);
            }
        } else {
            for (int i = id + 1; i < beans.size(); i++) {
                PlanBean planBean = beans.get(i);
                if (planBean.getPid() == id) {
                    loopUpdate(beans, planBean, curPoint, level + 1);
                    bean.setTotalCnt(bean.getTotalCnt() + planBean.getTotalCnt());
                    bean.setDoneCnt(bean.getDoneCnt() + planBean.getDoneCnt());
                }
            }
        }
    }


    /**
     * 设置 RecyclerView 的样式
     * 填充 list 内容
     */
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        recyclerView.setLayoutManager(layoutManager);
        LinkedList<PlanBrief> planBriefList = getPlanBriefList();
        planBriefList.sort(new Comparator<PlanBrief>() {
            @Override
            public int compare(PlanBrief planBrief, PlanBrief t1) {
                int a = planBrief.getTotal() - planBrief.getDone();
                int b = t1.getTotal() - t1.getDone();
                if (a == 0 && b != 0) return -1;
                return 1;
            }
        });
        adapter = new PlanBriefAdapter(getActivity(), planBriefList, rlEmpty);
        recyclerView.setAdapter(adapter);
        adapter.checkEmpty();

    }


    /**
     * 获取Plan的简介列表
     */
    private LinkedList<PlanBrief> getPlanBriefList() {
        LinkedList<PlanBrief> list = new LinkedList<>();
        for (Plan plan : My.plans) {
            list.add(plan.getPlanBrief());
        }
        return list;
    }


}
