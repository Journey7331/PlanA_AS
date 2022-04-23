package com.example.plana.function.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import com.example.plana.function.MainActivity;
import com.example.plana.bean.My;
import com.example.plana.config.Constant;
import com.example.plana.config.MyConfig;
import com.example.plana.R;
import com.example.plana.config.MyConfigConstant;
import com.example.plana.base.BaseActivity;
import com.example.plana.receiver.AlarmReceiver;
import com.example.plana.utils.TimeCalcUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @program: PlanA
 * @description: set Schedule Config
 */
public class ScheduleSettingActivity extends BaseActivity {

    protected static final String TAG = "ScheduleSettingActivity";

    // 用于存储, 读取本地配置文件
    protected static Map<String, String> configMap;

    // 用于应用配置
    protected static Map<String, Boolean> notConfigMap;

    static Boolean notIsOpen;
    static Boolean notIsShowWhen;
    static Boolean notIsShowWhere;
    static Boolean notIsShowStep;

    static Boolean hideWeekend;
    static Boolean hideNotThisWeek;
    static Boolean hideBlockTime;

    SwitchCompat switchNotOpen;
    SwitchCompat switchShowWhen;
    SwitchCompat switchShowWhere;
    SwitchCompat switchShowStep;

    EditText etSchoolStartTime;
    EditText etAlertTime;
    TextView tvNotify;
    Button btSubmitConfig;

    // 通知的时间
    int setHour = 22;
    int setMinute = 0;
    String alertTime = "22:00";
    String schoolStartTime = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_setting);

        //设置工具栏
        initToolbar("课程表设置");

        //初始化configMap<String, String>
        configMap = new HashMap<String, String>();

        etSchoolStartTime = findViewById(R.id.et_school_start_time);
        etAlertTime = findViewById(R.id.et_alert_time);
        tvNotify = findViewById(R.id.tv_notify_tip);
        btSubmitConfig = findViewById(R.id.btn_schedule_set_submit);

        // 初始化 SwitchCompat
        switchNotOpen = findViewById(R.id.switch_notification_tomorrow);
        switchShowWhen = findViewById(R.id.switch_notification_showWhen);
        switchShowWhere = findViewById(R.id.switch_notification_showWhere);
        switchShowStep = findViewById(R.id.switch_notification_showStep);
        SwitchCompat switchWeekend = findViewById(R.id.switch_hide_weekend);
        SwitchCompat switchNotThisWeek = findViewById(R.id.switch_hide_nonThis_week);
        SwitchCompat switchBlockTime = findViewById(R.id.switch_hide_block_time);

        //初始化noteConfigMap<String, Boolean>
        notConfigMap = MyConfig.getNotConfigMap();
        notIsOpen = notConfigMap.get(MyConfigConstant.CONFIG_NOT_OPEN);
        notIsShowWhen = notConfigMap.get(MyConfigConstant.CONFIG_NOT_SHOW_WHEN);
        notIsShowWhere = notConfigMap.get(MyConfigConstant.CONFIG_NOT_SHOW_WHERE);
        notIsShowStep = notConfigMap.get(MyConfigConstant.CONFIG_NOT_SHOW_STEP);
        hideWeekend = !notConfigMap.get(MyConfigConstant.CONFIG_SHOW_WEEKENDS);
        hideNotThisWeek = !notConfigMap.get(MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK);
        hideBlockTime = !notConfigMap.get(MyConfigConstant.CONFIG_SHOW_TIME);

        initHideSet();

        switchWeekend.setChecked(hideWeekend);
        switchNotThisWeek.setChecked(hideNotThisWeek);
        switchBlockTime.setChecked(hideBlockTime);

        switchNotOpen.setChecked(notIsOpen);
        switchShowWhen.setChecked(notIsShowWhen);
        switchShowWhere.setChecked(notIsShowWhere);
        switchShowStep.setChecked(notIsShowStep);

        if (!"".equals(schoolStartTime)) {
            etSchoolStartTime.setText(schoolStartTime);
        }
        if (!"22:00".equals(alertTime)) {
            etAlertTime.setText("提醒时间：" + alertTime);
            tvNotify.setText("每天" + alertTime + "推送第二天的课程信息");
        }

        etSchoolStartTime.setOnClickListener(l -> setupDate());
        etAlertTime.setOnClickListener(l -> setupTime());
        btSubmitConfig.setOnClickListener(l -> saveSettingConfig());

        //为switch添加监听器
        setNotOpenSwitch(switchNotOpen, setHour, setMinute);
        setNotSetSwitch(switchShowWhen);
        setNotSetSwitch(switchShowWhere);
        setNotSetSwitch(switchShowStep);

        setHideSetSwitch(switchWeekend);
        setHideSetSwitch(switchNotThisWeek);
        setHideSetSwitch(switchBlockTime);
    }


    /**
     * 存入设置的内容
     */
    private void saveSettingConfig() {

        // 再一次更新通知设置
        reStartRemind(notIsOpen, setHour, setMinute);

        // 存入设置
        MyConfig.saveConfig(configMap);

        // DEBUG
        Log.e(TAG, configMap.toString());

        // 跳转回 MainActivity
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
        }
        My.page = R.id.page_2;
        startActivity(new Intent(ScheduleSettingActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.slide_back2);
        finish();
    }

    /**
     * 设置课表appearance的初始化
     */
    private void initHideSet() {
        Map<String, String> myConfigMap = MyConfig.loadConfig();

        for (String key : myConfigMap.keySet()) {
            String value = myConfigMap.get(key);
            if (value == null)
                continue;
            switch (key) {
                case MyConfigConstant.CONFIG_ALERT_TIME:
                    alertTime = value;
                    break;
                case MyConfigConstant.CONFIG_START_DATE:
                    schoolStartTime = value.split(" ")[0];
                    break;
            }
        }
    }


    /**
     * 初始化 toolbar
     *
     * @param title toolbar标题
     */
    protected void initToolbar(String title) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) textView.getLayoutParams();
        layoutParams.setMarginStart(160);
        textView.setLayoutParams(layoutParams);
        textView.setText(title);
    }

    /**
     * 配置通知开启开关
     *
     * @param mySwitch  需要配置的开关
     * @param setHour   定时通知的时间-小时
     * @param setMinute 定时通知的时间-分钟
     */
    protected void setNotOpenSwitch(SwitchCompat mySwitch, int setHour, int setMinute) {

        mySwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        if (NotificationManagerCompat.from(ScheduleSettingActivity.this).areNotificationsEnabled()) {
                            //允许系统通知
                            startRemind(setHour, setMinute);
                            Toast.makeText(ScheduleSettingActivity.this, "通知已打开", Toast.LENGTH_SHORT).show();
                            notIsOpen = true;
                            configMap.put(MyConfigConstant.CONFIG_NOT_OPEN, MyConfigConstant.VALUE_TRUE);
                        } else {
                            //未允许系统通知
                            Toast.makeText(ScheduleSettingActivity.this, "请先在系统设置中允许通知", Toast.LENGTH_SHORT).show();
                            mySwitch.setChecked(false);
                        }
                    } else {
                        if (NotificationManagerCompat.from(ScheduleSettingActivity.this).areNotificationsEnabled()) {
                            //允许系统通知
                            stopRemind();
                            Toast.makeText(ScheduleSettingActivity.this, "通知已关闭", Toast.LENGTH_SHORT).show();
                            notIsOpen = false;
                            configMap.put(MyConfigConstant.CONFIG_NOT_OPEN, MyConfigConstant.VALUE_FALSE);

                            // 取消通知
                            switchShowWhen.setChecked(false);
                            switchShowWhere.setChecked(false);
                            switchShowStep.setChecked(false);
                        } else      //禁止系统通知
                            Toast.makeText(ScheduleSettingActivity.this, "请先在系统设置中允许通知", Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }


    protected void setHideSetSwitch(SwitchCompat switchCompat) {
        switchCompat.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            switch (switchCompat.getId()) {
                // 隐藏周末
                case R.id.switch_hide_weekend:
                    if (isChecked) {
                        Log.d(TAG, "hide_weekend set to on");
                        hideWeekend = true;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_WEEKENDS, MyConfigConstant.VALUE_FALSE);
                    } else {
                        Log.d(TAG, "hide_weekend set to off");
                        hideWeekend = false;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_WEEKENDS, MyConfigConstant.VALUE_TRUE);
                    }
                    break;
                // 隐藏非本周课程
                case R.id.switch_hide_nonThis_week:
                    if (isChecked) {
                        Log.d(TAG, "hide_nonThis_week set to on");
                        hideNotThisWeek = true;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK, MyConfigConstant.VALUE_FALSE);
                    } else {
                        Log.d(TAG, "hide_nonThis_week set to off");
                        hideNotThisWeek = false;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK, MyConfigConstant.VALUE_TRUE);
                    }
                    break;
                // 隐藏节次时间
                case R.id.switch_hide_block_time:
                    if (isChecked) {
                        Log.d(TAG, "hide_block_time set to on");
                        hideBlockTime = true;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_TIME, MyConfigConstant.VALUE_FALSE);
                    } else {
                        Log.d(TAG, "hide_block_time set to off");
                        hideBlockTime = false;
                        configMap.put(MyConfigConstant.CONFIG_SHOW_TIME, MyConfigConstant.VALUE_TRUE);
                    }
                    break;
            }
            // DEBUG
            Log.e(TAG, configMap.toString());
        });
    }


    /**
     * 配置通知设置开关
     *
     * @param mySwitch 需要配置的开关
     */
    protected void setNotSetSwitch(SwitchCompat mySwitch) {
        mySwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    switch (mySwitch.getId()) {
                        // 什么时候上课
                        case R.id.switch_notification_showWhen:
                            if (isChecked) {
                                Log.d(TAG, "showWhen set to on");
                                notIsShowWhen = true;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHEN, MyConfigConstant.VALUE_TRUE);
                            } else {
                                Log.d(TAG, "showWhen set to off");
                                notIsShowWhen = false;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHEN, MyConfigConstant.VALUE_FALSE);
                            }
                            break;
                        // 在哪里上课
                        case R.id.switch_notification_showWhere:
                            if (isChecked) {
                                Log.d(TAG, "showWhere set to on");
                                notIsShowWhere = true;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHERE, MyConfigConstant.VALUE_TRUE);
                            } else {
                                Log.d(TAG, "showWhere set to off");
                                notIsShowWhere = false;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_WHERE, MyConfigConstant.VALUE_FALSE);
                            }
                            break;
                        // 上多长时间的课
                        case R.id.switch_notification_showStep:
                            if (isChecked) {
                                Log.d(TAG, "showStep set to on");
                                notIsShowStep = true;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_STEP, MyConfigConstant.VALUE_TRUE);
                            } else {
                                Log.d(TAG, "showStep set to off");
                                notIsShowStep = false;
                                configMap.put(MyConfigConstant.CONFIG_NOT_SHOW_STEP, MyConfigConstant.VALUE_FALSE);
                            }
                            break;

                        default:
                            Log.d(TAG, "default;    info:" + mySwitch.getId());
                            break;
                    }
                    reStartRemind(notIsOpen, setHour, setMinute);
                    // 设定通知
                    if (isChecked) switchNotOpen.setChecked(true);
                }
        );
    }


    /**
     * 开启定时通知
     *
     * @param setHour   发出通知的时间-小时
     * @param setMinute 发出通知的时间-分钟
     */
    protected void startRemind(int setHour, int setMinute) {
        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        long systemTime = System.currentTimeMillis();
        // 是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, setHour);
        mCalendar.set(Calendar.MINUTE, setMinute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //**** AlarmReceiver.class为广播接受者 ****
        Intent intent = new Intent(ScheduleSettingActivity.this, AlarmReceiver.class);
        //intent添加额外信息
        intent.putExtra(MyConfigConstant.CONFIG_NOT_SHOW_WHEN, notIsShowWhen);
        intent.putExtra(MyConfigConstant.CONFIG_NOT_SHOW_WHERE, notIsShowWhere);
        intent.putExtra(MyConfigConstant.CONFIG_NOT_SHOW_STEP, notIsShowStep);
        PendingIntent pi = PendingIntent.getBroadcast(ScheduleSettingActivity.this, 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        //设定重复提醒，提醒周期为一天（24H）
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);

        Log.d(TAG, "开始倒计时提醒");
    }


    /**
     * 在修改通知内容后重新启动通知；若通知本身未开启，则不启动
     *
     * @param notIsOpen
     * @param setHour
     * @param setMinute
     */
    protected void reStartRemind(boolean notIsOpen, int setHour, int setMinute) {
        if (notIsOpen) {
            stopRemind();
            startRemind(setHour, setMinute);
        }
    }


    /**
     * 关闭通知
     */
    protected void stopRemind() {
        Intent intent = new Intent(ScheduleSettingActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ScheduleSettingActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        Log.d(TAG, "取消倒计时提醒");
    }


    /**
     * 选择开学日期： 年、月、日
     */
    private void setupDate() {
        Calendar cal = Calendar.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_date, null);
        DatePicker datePicker = view.findViewById(R.id.add_date_picker);

        AlertDialog alertDialog = new AlertDialog
                .Builder(ScheduleSettingActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                    schoolStartTime = TimeCalcUtil.calToStr(cal);
                    configMap.put(MyConfigConstant.CONFIG_START_DATE, schoolStartTime);
                    configMap.put(MyConfigConstant.CONFIG_CUR_WEEK, schoolStartTime);
                    MyConfig.saveConfig(configMap);     // 保存设置信息至本地配置文件
                    etSchoolStartTime.setText(TimeCalcUtil.calToSimpleStr(cal));
                }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);

    }

    /**
     * 选择提醒日期： 时、分
     */
    @SuppressLint("SetTextI18n")
    private void setupTime() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_time, null);
        TimePicker timePicker = view.findViewById(R.id.add_time_picker);
        timePicker.setIs24HourView(true);

        AlertDialog alertDialog = new AlertDialog
                .Builder(ScheduleSettingActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    setHour = timePicker.getHour();
                    setMinute = timePicker.getMinute();

                    StringBuilder str = new StringBuilder().append(setHour).append(':');
                    if (setMinute < 10) str.append("0");
                    alertTime = str.append(setMinute).toString();

                    configMap.put(MyConfigConstant.CONFIG_ALERT_TIME, alertTime);

                    etAlertTime.setText("提醒时间：" + alertTime);
                    tvNotify.setText("每天" + alertTime + "推送第二天的课程信息");

                }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);

    }

}
