package com.example.plana.config;

/**
 * @program: PlanA
 * @description:
 */
public class MyConfigConstant {

    public class Todo{
        /**
         * 待办显示 config
         */
        public static final String CONFIG_HIDE_DONE_TODO = "config_hide_done_todo";

        /**
         * 待办排序 config
         */
        public static final String CONFIG_TODO_SORT = "config_todo_sort";

        public static final String SORT_BY_ADD_TIME = "sort_by_add_time";
        public static final String SORT_BY_DATE = "sort_by_date";
        public static final String SORT_BY_PRIORITY = "sort_by_priority";
    }

    /**
     * 课程表显示 Config
     * */
    public static final String CONFIG_SHOW_WEEKENDS = "config_show_weekends";
    public static final String CONFIG_SHOW_NOT_CUR_WEEK = "config_show_not_cur_week";
    public static final String CONFIG_SHOW_TIME = "config_show_time";

    /**
     * 课程通知 Config
     * */
    public static final String CONFIG_NOT_OPEN = "config_not_open";
    public static final String CONFIG_NOT_SHOW_WHERE = "config_not_where";
    public static final String CONFIG_NOT_SHOW_WHEN = "config_not_when";
    public static final String CONFIG_NOT_SHOW_STEP = "config_not_step";

    /**
     * 存储的是开学日期，需利用其他工具动态计算当前周;
     * 存储格式"yy-MM-dd HH:mm:ss"
     */
    public static final String CONFIG_CUR_WEEK = "config_current_week";

    /**
     * 存储课程通知提醒时间
     * 存储格式为"HH:mm"
     * */
    public static final String CONFIG_ALERT_TIME = "config_alert_time";

    /**
     * 存储开学的时间，是用户输入的时间点
     * 存储格式为"HH:mm"
     * */
    public static final String CONFIG_START_DATE = "config_start_date";

    /**
     * 自定义 boolean 类型
     * */
    public static final String VALUE_TRUE = "config_value_true";
    public static final String VALUE_FALSE = "config_value_false";

}
