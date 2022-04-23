package com.example.plana.config;

import android.graphics.Color;

/**
 * @program: PlanA
 * @description:
 */
public class Constant {

    /**
     * 服务器的 URL
     */
    public static final String URL = "http://192.168.1.105：8080/";


    /**
     * LeanCloud setting
     */
    public static class LC {
        public static final String AppId = "ihfRp7N9zo8JIFXuw34r8C2s-gzGzoHsz";
        public static final String AppKey = "8KkXnf5IEBByDyxPzpIeDWAP";
        public static final String serverURL = "https://ihfrp7n9.lc-cn-n1-shared.com";
    }


    /**
     * 注意这里的 Color 类是 android.graphics.Color
     * 不是 java.awt.Color
     * <p>
     * 常用的 颜色
     */
    public static class MyColor {
        public static final int BlueGrey = Color.rgb(82, 125, 114);
        public static final int MyRed = Color.rgb(221, 0, 27);
    }

    public static class TAG {
        public static final String NOTIFY_TAG = "NOTIFY_TAG";
        public static final String DATE_BASE_TAG = "DATE_BASE_TAG";
    }


    public static class Timer {
        /**
         * 正计时
         */
        public static final String POSITIVE = "POSITIVE";

        /**
         * 倒计时
         */
        public static final String NEGATIVE = "NEGATIVE";
    }


}
