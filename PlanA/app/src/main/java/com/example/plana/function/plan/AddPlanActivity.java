package com.example.plana.function.plan;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.adapter.AddPlanAdapter;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.bean.Plan;
import com.example.plana.function.fragment.PlanFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: PlanA
 * @description:
 */
public class AddPlanActivity extends BaseActivity {

    EditText etCopyBoard;
    Button btSubmit;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        etCopyBoard = findViewById(R.id.et_copy_board);
        btSubmit = findViewById(R.id.bt_add_plan_submit);
        listView = findViewById(R.id.lv_add_plan_list);

        listView.setAdapter(new AddPlanAdapter(AddPlanActivity.this, getPlans()));

        btSubmit.setOnClickListener(l -> {
            if (etCopyBoard.getText().length() == 0) {
                Toast.makeText(AddPlanActivity.this, "未输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isJSON(etCopyBoard.getText().toString())) {
                Plan plan = (Plan) new Gson().fromJson(etCopyBoard.getText().toString(), new TypeToken<Plan>() {
                }.getType());
                if (plan != null) {
                    for (Plan p : My.plans) {
                        if (p.getPlanId() == plan.getPlanId()) {
                            etCopyBoard.setText("");
                            Toast.makeText(AddPlanActivity.this, "已有该计划哦", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    My.plans.add(plan);
                    Toast.makeText(AddPlanActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
//                    System.out.println(plan);
                    finish();
                } else {
                    etCopyBoard.setText("");
                    Toast.makeText(AddPlanActivity.this, "解析失败，请重试", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                etCopyBoard.setText("");
                Toast.makeText(AddPlanActivity.this, "格式有误，请重新复制", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        findViewById(R.id.bt_add_plan_back).setOnClickListener(l -> finish());
    }


    /**
     * 是否是JSON字符串的表达式
     */
    public boolean isJSON(String value) {
        try {
            boolean result = false;
            String jsonRegexp = "^(?:(?:\\s*\\[\\s*(?:(?:"
                    + "(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json1>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*,\\s*)*(?:"
                    + "(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json2>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*\\]\\s*)"
                    + "|(?:\\s*\\{\\s*"
                    + "(?:\"[^\"]*?\"\\s*:\\s*(?:(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json3>(?:\\[.*?\\])|(?:\\{.*?\\})))\\s*,\\s*)*"
                    + "(?:\"[^\"]*?\"\\s*:\\s*(?:(?:\"[^\"]*?\")|(?:true|false|null)|(?:[+-]?\\d+(?:\\.?\\d+)?(?:[eE][+-]?\\d+)?)|(?<json4>(?:\\[.*?\\])|(?:\\{.*?\\}))))\\s*\\}\\s*))$";

            Pattern jsonPattern = Pattern.compile(jsonRegexp);

            Matcher jsonMatcher = jsonPattern.matcher(value);

            if (jsonMatcher.matches()) {
                result = true;
                for (int i = 4; i >= 1; i--) {
                    if (!StringUtils.isEmpty(jsonMatcher.group("json" + i))) {
                        result = this.isJSON(jsonMatcher.group("json" + i));
                        if (!result) {
                            break;
                        }
                        if (i == 3 || i == 1) {
                            result = this.isJSON(value.substring(0, jsonMatcher.start("json" + i))
                                    + (i == 3 ? "\"JSON\"}" : "\"JSON\"]"));
                            if (!result) {
                                break;
                            }
                        }
                    }
                }

            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取模版计划
     * */
    private List<Plan> getPlans() {
        ArrayList<String> planStrings = new ArrayList<>();
        planStrings.add("{\"planId\":1,\"description\":\"JavaSE入门\",\"img_id\":3,\"curPoint\":0,\"planBeans\":[{\"id\":0,\"pid\":-1,\"title\":\"JavaSE\",\"level\":0,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":1,\"pid\":0,\"title\":\"Java 基础语法篇\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":2,\"pid\":0,\"title\":\"Java 高级语法篇\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":3,\"pid\":1,\"title\":\"1 语言概述\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":4,\"pid\":1,\"title\":\"2 Java 基本语法\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":5,\"pid\":1,\"title\":\"3 Java 数组\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":6,\"pid\":1,\"title\":\"4 面向对象（上）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":7,\"pid\":1,\"title\":\"5 面向对象（中）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":8,\"pid\":1,\"title\":\"6 面向对象（下）\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":9,\"pid\":2,\"title\":\"1 异常处理\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":10,\"pid\":2,\"title\":\"2 多线程\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":11,\"pid\":2,\"title\":\"3 常用类\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":12,\"pid\":2,\"title\":\"4 枚举类与注解的使用\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":13,\"pid\":2,\"title\":\"5 集合\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":14,\"pid\":2,\"title\":\"6 泛型\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":15,\"pid\":2,\"title\":\"7 IO流\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":16,\"pid\":2,\"title\":\"8 网络编程\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":17,\"pid\":2,\"title\":\"9 反射机制\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":18,\"pid\":3,\"title\":\"1 背景介绍\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":19,\"pid\":3,\"title\":\"2 java的特性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":20,\"pid\":3,\"title\":\"3 环境变量的配置\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":21,\"pid\":3,\"title\":\"4 第一个 HelloWorld\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":22,\"pid\":4,\"title\":\"1 关键字与保留字\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":23,\"pid\":4,\"title\":\"2 标识符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":24,\"pid\":4,\"title\":\"3 变量\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":25,\"pid\":4,\"title\":\"4 编码\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":26,\"pid\":4,\"title\":\"5 字符串\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":27,\"pid\":4,\"title\":\"6 进制\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":28,\"pid\":4,\"title\":\"7 运算符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":29,\"pid\":4,\"title\":\"8 程序流程控制\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":30,\"pid\":5,\"title\":\"1 数组的常见概念\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":31,\"pid\":5,\"title\":\"2 一维数组\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":32,\"pid\":5,\"title\":\"3 多维数组\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":33,\"pid\":5,\"title\":\"4 排序简述\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":34,\"pid\":5,\"title\":\"5 Arrays工具类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":35,\"pid\":6,\"title\":\"1 面向对象概述\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":36,\"pid\":6,\"title\":\"2 类和对象\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":37,\"pid\":6,\"title\":\"3 对象的创建和使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":38,\"pid\":6,\"title\":\"4 属性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":39,\"pid\":6,\"title\":\"5 方法\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":40,\"pid\":6,\"title\":\"6 封装\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":41,\"pid\":6,\"title\":\"7 构造器\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":42,\"pid\":7,\"title\":\"1 继承\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":43,\"pid\":7,\"title\":\"2 方法的重写\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":44,\"pid\":7,\"title\":\"3 权限修饰符\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":45,\"pid\":7,\"title\":\"4 实例化\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":46,\"pid\":7,\"title\":\"5 多态\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":47,\"pid\":7,\"title\":\"6 Object类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":48,\"pid\":8,\"title\":\"1 抽象类与抽象方法\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":49,\"pid\":8,\"title\":\"2 接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":50,\"pid\":8,\"title\":\"3 内部类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":51,\"pid\":9,\"title\":\"1 常见异常\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":52,\"pid\":9,\"title\":\"2 try-catch-finally\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":53,\"pid\":9,\"title\":\"3 throws\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":54,\"pid\":9,\"title\":\"4 手动抛出异常：throw\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":55,\"pid\":10,\"title\":\"1 基本概念：程序、进程、线程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":56,\"pid\":10,\"title\":\"2 线程的创建和使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":57,\"pid\":10,\"title\":\"3 线程的生命周期\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":58,\"pid\":10,\"title\":\"4 线程的同步\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":59,\"pid\":10,\"title\":\"5 线程的通信\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":60,\"pid\":11,\"title\":\"1 字符串相关类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":61,\"pid\":11,\"title\":\"2 日期时间API\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":62,\"pid\":11,\"title\":\"3 Java比较器\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":63,\"pid\":11,\"title\":\"4 System类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":64,\"pid\":12,\"title\":\"1 枚举的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":65,\"pid\":12,\"title\":\"2 注解的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":66,\"pid\":13,\"title\":\"1 Collection接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":67,\"pid\":13,\"title\":\"2 Iterator迭代器接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":68,\"pid\":13,\"title\":\"3 List接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":69,\"pid\":13,\"title\":\"4 Set接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":70,\"pid\":13,\"title\":\"5 Map接口\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":71,\"pid\":13,\"title\":\"6 Collections工具类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":72,\"pid\":14,\"title\":\"1 在集合中使用泛型\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":73,\"pid\":14,\"title\":\"2 自定义泛型结构\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":74,\"pid\":14,\"title\":\"3 通配符的使用\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":75,\"pid\":15,\"title\":\"1 File类\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":76,\"pid\":15,\"title\":\"2 IO流原理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":77,\"pid\":15,\"title\":\"3 文件流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":78,\"pid\":15,\"title\":\"4 缓冲流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":79,\"pid\":15,\"title\":\"5 转换流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":80,\"pid\":15,\"title\":\"6 输入、输出流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":81,\"pid\":15,\"title\":\"7 打印流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":82,\"pid\":15,\"title\":\"8 数据流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":83,\"pid\":15,\"title\":\"9 对象流\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":84,\"pid\":16,\"title\":\"1 IP和端口号\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":85,\"pid\":16,\"title\":\"2 网络协议\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":86,\"pid\":16,\"title\":\"3 TCP网络编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":87,\"pid\":16,\"title\":\"4 UDP网络编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":88,\"pid\":16,\"title\":\"5 URL编程\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":89,\"pid\":17,\"title\":\"1 获取Class实例\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":90,\"pid\":17,\"title\":\"2 类加载ClassLoader\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":91,\"pid\":17,\"title\":\"3 动态代理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true}],\"planBrief\":{\"planId\":1,\"planName\":\"JavaSE\",\"total\":74,\"done\":20}}");
        planStrings.add("{\"planId\":2,\"description\":\"Redis\",\"img_id\":1,\"curPoint\":0,\"planBeans\":[{\"id\":0,\"pid\":-1,\"title\":\"Redis\",\"level\":0,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":1,\"pid\":0,\"title\":\"数据结构和缓存\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":2,\"pid\":0,\"title\":\"高可靠、高性能\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":3,\"pid\":0,\"title\":\"Redis 底层实现原理\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":4,\"pid\":1,\"title\":\"1 常见数据类型\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":5,\"pid\":1,\"title\":\"2 扩展数据类型\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":6,\"pid\":1,\"title\":\"3 缓存\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":7,\"pid\":2,\"title\":\"1 持久化机制\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":8,\"pid\":2,\"title\":\"2 主从复制机制\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":9,\"pid\":2,\"title\":\"3 哨兵机制\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":10,\"pid\":2,\"title\":\"4 故障自动恢复\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":11,\"pid\":2,\"title\":\"5 切片集群\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":12,\"pid\":3,\"title\":\"1 分布式\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":13,\"pid\":3,\"title\":\"2 哨兵集群\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":14,\"pid\":4,\"title\":\"1 String\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":15,\"pid\":4,\"title\":\"2 List\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":16,\"pid\":4,\"title\":\"3 Hash\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":17,\"pid\":4,\"title\":\"4 Set\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":18,\"pid\":4,\"title\":\"5 Sorted Set\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":19,\"pid\":5,\"title\":\"1 HyperLongLog\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":20,\"pid\":5,\"title\":\"2 Bitmap\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":21,\"pid\":5,\"title\":\"3 GEO\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":22,\"pid\":6,\"title\":\"1 缓存击穿\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":23,\"pid\":6,\"title\":\"2 缓存穿透\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":24,\"pid\":6,\"title\":\"3 缓存雪崩\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":25,\"pid\":7,\"title\":\"1 RDB\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":26,\"pid\":7,\"title\":\"2 AOF\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":27,\"pid\":7,\"title\":\"3 混合持久化\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":28,\"pid\":12,\"title\":\"1 CAP\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":29,\"pid\":12,\"title\":\"2 分布式锁\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":30,\"pid\":12,\"title\":\"3 info指令\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":31,\"pid\":12,\"title\":\"4 主从复制\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":32,\"pid\":12,\"title\":\"5 哨兵\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":33,\"pid\":12,\"title\":\"6 集群\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true}],\"planBrief\":{\"planId\":2,\"planName\":\"Redis学习路径\",\"total\":25,\"done\":0}}");
        planStrings.add("{\"planId\":3,\"description\":\"考研数学\",\"img_id\":2,\"curPoint\":0,\"planBeans\":[{\"id\":0,\"pid\":-1,\"title\":\"考研数学（一）\",\"level\":0,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":1,\"pid\":0,\"title\":\"一 函数\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":2,\"pid\":0,\"title\":\"二 极限\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":3,\"pid\":0,\"title\":\"三 连续\",\"level\":1,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":4,\"pid\":1,\"title\":\"1 性质\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":5,\"pid\":1,\"title\":\"2 初等函数\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":6,\"pid\":1,\"title\":\"3 常见形式\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":7,\"pid\":2,\"title\":\"1 概念\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":8,\"pid\":2,\"title\":\"2 性质\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":9,\"pid\":2,\"title\":\"3 计算\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":10,\"pid\":3,\"title\":\"1 连续\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":11,\"pid\":3,\"title\":\"2 间断\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":12,\"pid\":3,\"title\":\"3 性质\",\"level\":2,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":false},{\"id\":13,\"pid\":4,\"title\":\"1 有界性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":14,\"pid\":4,\"title\":\"2 单调性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":15,\"pid\":4,\"title\":\"3 奇偶性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":16,\"pid\":4,\"title\":\"4 周期性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":17,\"pid\":5,\"title\":\"1 基本初等函数\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":18,\"pid\":5,\"title\":\"2 复合函数\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":19,\"pid\":6,\"title\":\"1 分段函数\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":20,\"pid\":6,\"title\":\"2 反函数\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":21,\"pid\":6,\"title\":\"3 隐函数\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":22,\"pid\":7,\"title\":\"1 定义\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":23,\"pid\":7,\"title\":\"2 充要条件\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":24,\"pid\":7,\"title\":\"3 特殊极限\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":25,\"pid\":8,\"title\":\"1 唯一性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":26,\"pid\":8,\"title\":\"2 有界性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":27,\"pid\":8,\"title\":\"3 局部保号性\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":28,\"pid\":9,\"title\":\"1 运算法则\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":29,\"pid\":9,\"title\":\"2 极限的计算步骤\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":30,\"pid\":9,\"title\":\"3 单调有界规则\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":31,\"pid\":9,\"title\":\"4 夹逼准则\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":32,\"pid\":10,\"title\":\"1 概念\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":33,\"pid\":10,\"title\":\"2 运算\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":34,\"pid\":11,\"title\":\"1 概念\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":35,\"pid\":11,\"title\":\"2 寻找特殊点\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":36,\"pid\":11,\"title\":\"3 特殊点的类型\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":37,\"pid\":12,\"title\":\"1 最值定理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":38,\"pid\":12,\"title\":\"2 有界性定理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":39,\"pid\":12,\"title\":\"3 介值定理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true},{\"id\":40,\"pid\":12,\"title\":\"4 零点定理\",\"level\":3,\"totalCnt\":0,\"doneCnt\":0,\"done\":false,\"isLeaf\":true}],\"planBrief\":{\"planId\":3,\"planName\":\"考研数学\",\"total\":28,\"done\":0}}");

        List<Plan> plans = new ArrayList<>();
        for (String planString : planStrings) {
            plans.add(
                    new Gson().fromJson(planString, new TypeToken<Plan>() {
                    }.getType())
            );
        }
        return plans;
    }


}
