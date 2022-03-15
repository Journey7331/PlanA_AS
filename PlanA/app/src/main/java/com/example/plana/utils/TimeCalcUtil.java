package com.example.plana.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @program: PlanA
 * @description: 时间相关的计算工具
 */
public class TimeCalcUtil {

    /**
     * 将"yyyy-MM-dd HH:mm:ss"格式的时间字符串 转换成 Date型
     */
    public static Date str2Date(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将Date型 转换成 "yyyy-MM-dd HH:mm:ss"格式的时间字符串
     */
    public static String date2Str(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    /**
     * 计算某一时间 某几周前的Date:
     * 计算date时间在weeksNum周前的Date
     * <p>
     * (注意！！输入的weeksNum为Integer，计算时间戳的差需要转为Long)
     */
    public static Date calWeeksAgo(Date date, int weeksNum) {
        long back = ((long) weeksNum) * 7 * 24 * 60 * 60 * 1000;
        return new Date(date.getTime() - back);
    }


    /**
     * 计算还剩几天或者过去了多长时间
     * 返回一个显示在 EventAdapter 上的字符串
     */
    public static String leftTime(long late, long early) {
        long diff = late - early;
        // Day
        long days = diff / (1000 * 60 * 60 * 24);
        if (days != 0) return days + "D";
        // Hour
        long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        if (hours != 0) return hours + "H";
        // Min
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        if (minutes != 0) return minutes + "M";
        // Sec
        long seconds = (diff % (1000 * 60)) / 1000;
        if (seconds < 3) return "Now";
        else return "1M";
    }

}
