package com.example.plana.adapter;

import android.text.TextUtils;
import android.widget.LinearLayout;

import com.zhuangfei.timetable.listener.OnDateBuildAapter;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.List;
import java.text.SimpleDateFormat;

/**
 * @program: PlanA
 * @description: 自定义日期栏
 */
public class OnDateDelayAdapter extends OnDateBuildAapter {

    /**
     * 阈值，即超过这个时间戳后开始更新日期
     * 否则将一直显示initDates中的日期
     */
    protected long startTime;
    protected String startTimeStr;

    protected SimpleDateFormat sdf;

    /**
     * 日期集合,8个元素，当前时间小于等于阈值时使用
     */
    List<String> initDates = null;

    public OnDateDelayAdapter() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * 设置日期集合
     *
     * @param dates 元素个数必须大于等于8，第一个为月份数值，第2-8为周一至周日的日期数值（不带中文）
     */
    public void setDateList(List<String> dates) {
        if (dates.size() >= 8) {
            this.initDates = dates;
        }
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        this.startTimeStr = sdf.format(startTime);
    }

    @Override
    public void onInit(LinearLayout layout, float alpha) {
        super.onInit(layout, alpha);

        //增加的
        long curTime = System.currentTimeMillis();
        if (curTime <= startTime) {
            weekDates = initDates;
        }
    }

    @Override
    public void onUpdateDate(int curWeek, int targetWeek) {
        if (textViews == null || textViews.length < 8) return;

        if (whenBeginSchool() <= 0) {
            weekDates = ScheduleSupport.getDateStringFromWeek(curWeek, targetWeek);
        }

//        weekDates = ScheduleSupport.getDateStringFromWeek(curWeek,targetWeek);
        int month = Integer.parseInt(weekDates.get(0));
        textViews[0].setText(month + "\n月");
        for (int i = 1; i < 8; i++) {
            if (textViews[i] != null) {
                textViews[i].setText(weekDates.get(i) + "日");
            }
        }
    }

    /**
     * 计算距离开学的天数
     *
     * @return 返回值2种类型，-1:没有开学时间，无法计算；0：已经开学；>0:天数
     */
    public long whenBeginSchool() {
        if (!TextUtils.isEmpty(startTimeStr)) {
            int calWeek = ScheduleSupport.timeTransfrom(startTimeStr);
            if (calWeek > 0) {//开学
                return 0;
            } else {
                long seconds = (startTime - System.currentTimeMillis()) / 1000;
                long day = seconds / (24 * 3600);
                return day;
            }
        }
        return -1;
    }
}
