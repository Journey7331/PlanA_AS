package com.example.plana.config;

import com.example.plana.fragment.ScheduleFragment;
import com.example.plana.utils.ContextApplication;
import com.example.plana.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: PlanA
 * @description:
 */
public class MyConfig {

    public static final String TAG = "MyConfig";

    /**
     * 保存当前配置信息（缓冲map）至本地文件
     *
     * @param configMap 配置缓冲map
     */
    public static void saveConfig(Map<String, String> configMap) {
        SharedPreferencesUtil sharedPreferencesUtil =
                SharedPreferencesUtil.init(ContextApplication.getAppContext(), ScheduleFragment.CONFIG_FILENAME);
        for (String key : configMap.keySet()) {
            String value = configMap.get(key);
            sharedPreferencesUtil.putString(key, value);
        }
    }


    /**
     * 从本地配置文件中读取信息至缓冲map
     */
    public static Map<String, String> loadConfig() {
        Map<String, String> configMap;
        SharedPreferencesUtil sharedPreferencesUtil =
                SharedPreferencesUtil.init(ContextApplication.getAppContext(), ScheduleFragment.CONFIG_FILENAME);
        configMap = (Map<String, String>) sharedPreferencesUtil.getAll();
        return configMap;
    }


    /**
     * 从本地配置文件里获取notConfigMap;
     * 默认value都是false
     *
     * @return
     */
    public static Map<String, Boolean> getNotConfigMap() {
        Map<String, String> originMap = MyConfig.loadConfig();
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
