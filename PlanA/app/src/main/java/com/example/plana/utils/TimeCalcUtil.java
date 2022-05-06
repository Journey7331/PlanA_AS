package com.example.plana.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * 返回一个显示在 TodoAdapter 上的字符串
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


    /**
     * Todo条目中显示的时间
     */
    public static String getTodoDateStr(String date, String time) {
        if (StringUtils.isEmpty(date) || "".equals(date)) return "";
        long dateParse = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            dateParse = sdf.parse(date).getTime();
            if (StringUtils.isNotEmpty(time) && !"".equals(time)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);
                dateParse += simpleDateFormat.parse(time).getTime();
                dateParse += 8 * 60 * 60 * 1000;        // 转化 time 需要加上 8 hours 【WHY?】
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long nowTime = new Date().getTime();
        if (nowTime > dateParse) {
            String calc = TimeCalcUtil.leftTime(nowTime, dateParse);
            // Today but no exact Time
            if (calc.contains("H") && "".equals(time)) {
                return "今天";
            } else {
                return "-" + calc;
            }
        } else if (nowTime < dateParse) {
            return TimeCalcUtil.leftTime(dateParse, nowTime);
        }
        return "";
    }


    /**
     * 将 Calendar 型 转换成 "yyyy-MM-dd HH:mm:ss"格式的时间字符串
     */
    public static String calToStr(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(cal.getTime());
    }

    /**
     * 将 Calendar 型 转换成 "yyyy-MM-dd"格式的时间字符串
     */
    public static String calToSimpleStr(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(cal.getTime());
    }

    /**
     * 将 Calendar 型 转换成 "HH:mm"格式的时间字符串
     */
    public static String calToHourMinStr(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(cal.getTime());
    }

    /**
     * 计算计划的剩余日期
     * */
    public static String getLeftDay(int days) {
        if (days == 0) {
            return "已完成";
        }
        return "还剩" + days + "天";
    }


    /**
     * 计算多少分钟后是什么时候
     * 返回值 "HH:mm"
     * 如果是下一天，在末尾加上「第二天」
     */
    public static String calcEndTime(int startHour, int startMinute, int time) {
        StringBuilder endTime = new StringBuilder();
        boolean flag = false;

        int endMinute = startMinute + time;
        int endHour = startHour + endMinute / 60;
        endMinute %= 60;

        if (endHour >= 24) {
            endHour %= 24;
            flag = true;
        }
        if (endHour < 10) endTime.append("0");
        endTime.append(endHour).append(":");

        if (endMinute<10) endTime.append("0");
        endTime.append(endMinute);

        if (flag) endTime.append(" (+1)");

        return endTime.toString();
    }

    /**
     * 拼接时间
     * */
    public static String format02d(int Hour, int Minute) {
        StringBuilder startTime = new StringBuilder();

        if (Hour < 10) startTime.append("0");
        startTime.append(Hour).append(":");

        if (Minute<10) startTime.append("0");
        startTime.append(Minute);

        return startTime.toString();
    }

    /**
     * 转为两位数
     * */
    public static String format02d(int num) {
        if (num >= 10) return String.valueOf(num);
        return "0" + num;
    }


}
