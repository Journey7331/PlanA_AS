package com.example.plana.config;

import com.example.plana.base.MainApplication;
import com.example.plana.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: PlanA
 * @description:
 */
public class MyConfig {

    public static final String TAG = "MyConfig";
    public static final String SCHEDULE_CONFIG_FILENAME = "myConfig";
    public static final String TODO_CONFIG_FILENAME = "todoConfig";

    /**
     * 保存当前配置信息（缓冲map）至本地文件
     *
     * @param configMap 配置缓冲map
     */
    public static void saveConfig(Map<String, String> configMap, String fileName) {
        SharedPreferencesUtil sharedPreferencesUtil =
                SharedPreferencesUtil.init(MainApplication.getAppContext(), fileName);
        for (String key : configMap.keySet()) {
            String value = configMap.get(key);
            sharedPreferencesUtil.putString(key, value);
        }
    }

    public static void saveScheduleConfig(Map<String, String> configMap) {
        saveConfig(configMap, SCHEDULE_CONFIG_FILENAME);
    }

    public static void saveTodoConfig(Map<String, String> configMap) {
        saveConfig(configMap, TODO_CONFIG_FILENAME);
    }


    /**
     * 从本地配置文件中读取信息至缓冲map
     */
    public static Map<String, String> loadConfig(String fileName) {
        Map<String, String> configMap;
        SharedPreferencesUtil sharedPreferencesUtil =
                SharedPreferencesUtil.init(MainApplication.getAppContext(), fileName);
        configMap = (Map<String, String>) sharedPreferencesUtil.getAll();
        return configMap;
    }

    public static Map<String, String> loadScheduleConfig() {
        return loadConfig(SCHEDULE_CONFIG_FILENAME);
    }

    /**
     * 返回todo的配置，添加初始配置
     * */
    public static Map<String, String> loadTodoConfig() {
        Map<String, String> map = loadConfig(TODO_CONFIG_FILENAME);
        Map<String, String> todoMap = new HashMap<>();

        String value = map.get(MyConfigConstant.Todo.CONFIG_HIDE_DONE_TODO);
        if (value == null) value = MyConfigConstant.VALUE_FALSE;
        todoMap.put(MyConfigConstant.Todo.CONFIG_HIDE_DONE_TODO, value);

        value = map.get(MyConfigConstant.Todo.CONFIG_TODO_SORT);
        if (value == null) value = MyConfigConstant.Todo.SORT_BY_ADD_TIME;
        todoMap.put(MyConfigConstant.Todo.CONFIG_TODO_SORT, value);

        return todoMap;
    }


    /**
     * 从本地配置文件里获取notConfigMap;
     * 默认value都是false
     *
     * @return
     */
    public static Map<String, Boolean> getNotConfigMap() {
        // 【 String, String 】
        Map<String, String> originMap = MyConfig.loadScheduleConfig();
        // 【 String, Boolean 】
        Map<String, Boolean> notConfigMap = new HashMap<>();
        //初始化
        notConfigMap.put(MyConfigConstant.CONFIG_NOT_OPEN, false);
        notConfigMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHEN, false);
        notConfigMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHERE, false);
        notConfigMap.put(MyConfigConstant.CONFIG_NOT_SHOW_STEP, false);
        notConfigMap.put(MyConfigConstant.CONFIG_SHOW_WEEKENDS, true);
        notConfigMap.put(MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK, true);
        notConfigMap.put(MyConfigConstant.CONFIG_SHOW_TIME, true);
        //从配置文件里读取
        for (String key : originMap.keySet()) {
            String value = originMap.get(key);
            if (value == null)
                continue;
            switch (key) {
                case MyConfigConstant.CONFIG_NOT_OPEN:
                case MyConfigConstant.CONFIG_NOT_SHOW_WHEN:
                case MyConfigConstant.CONFIG_NOT_SHOW_WHERE:
                case MyConfigConstant.CONFIG_NOT_SHOW_STEP:
                case MyConfigConstant.CONFIG_SHOW_WEEKENDS:
                case MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK:
                case MyConfigConstant.CONFIG_SHOW_TIME:
                    notConfigMap.put(
                            key,
                            value.equals(MyConfigConstant.VALUE_TRUE)
                    );
                    break;
            }
        }
        return notConfigMap;
    }


}
