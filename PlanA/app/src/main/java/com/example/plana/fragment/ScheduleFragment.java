package com.example.plana.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.example.plana.activity.AddCourseActivity;
import com.example.plana.config.MyConfig;
import com.example.plana.R;
import com.example.plana.activity.MainActivity;
import com.example.plana.adapter.OnDateDelayAdapter;
import com.example.plana.config.OnMyConfigHandleAdapter;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.MySubject;
import com.example.plana.utils.ContextApplication;
import com.example.plana.utils.SharedPreferencesUtil;
import com.example.plana.utils.SubjectRepertory;
import com.example.plana.utils.TimeCalcUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnSlideBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * @program: PlanA
 * @description:
 */
public class ScheduleFragment extends BaseFragment
        implements View.OnClickListener {

    private static final String TAG = "ScheduleFragment";
    public static final String CONFIG_FILENAME = "myConfig";    // 本地配置文件 文件名称

    TimetableView timetableView;
    WeekView weekView;
    List<MySubject> mySubjects;

    MyConfig myConfig;  // 存放配置信息
    Map<String, String> myConfigMap;

    LinearLayout layout;
    TextView titleTextView;

    // TODO check NumberPickerView source
    NumberPickerView yearPicker;
    NumberPickerView monthPicker;
    NumberPickerView dayPicker;

    int target = -1;     // 记录切换的周次，不一定是当前周
    String startDate = "2021-3-1"; // 开学时间


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        titleTextView = view.findViewById(R.id.id_title);

        // 点击 title layout 查看周预览
        layout = view.findViewById(R.id.id_layout);
        layout.setOnClickListener(this);

        loadSubjects();     // 加载课程
        initTimetableView(view);  // 初始化界面
        loadLocalConfig();    // 读取本地配置文件

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        //用于更正日期的显示
        int cur = timetableView.curWeek();
        timetableView.onDateBuildListener().onUpdateDate(cur, cur);

        //更新文本
        OnDateDelayAdapter adapter = (OnDateDelayAdapter) timetableView.onDateBuildListener();
        long when = adapter.whenBeginSchool();
        if (when > 0) {
            String str = "距离开学还有" + when + "天";
            titleTextView.setText(str);
        }

        //更新map
        myConfigMap = MyConfig.loadConfig();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (weekView.isShowing()) hideWeekView();
                else showWeekView();
                break;
        }
    }


    /**
     * 载入课程数据到list中
     */
    private void loadSubjects() {
        String subjectListJson = SharedPreferencesUtil.init(ContextApplication.getAppContext(),"COURSE_DATA").getString("SUBJECT_LIST", null);
        if (subjectListJson == null) {
            mySubjects = SubjectRepertory.loadDefaultSubjects();
            if (!mySubjects.isEmpty()) {
                toSaveSubjects(mySubjects);
            }
        } else {
            mySubjects = toGetSubjects();
        }
    }


    /**
     * 初始化课程控件
     */
    protected void initTimetableView(View view) {
        //获取控件
        weekView = view.findViewById(R.id.id_weekview);
        timetableView = view.findViewById(R.id.id_timetableView);

        //设置周次选择属性
        weekView.source(mySubjects)
                //     .curWeek(1)
                .itemCount(25)  // 周数
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = timetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        timetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        timetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();

        timetableView.source(mySubjects)  // 课程源
                //      .curWeek(1)
                .curTerm(null)     // 当前学期
                .maxSlideItem(12)  // 节数
                .monthWidthDp(40)  // 月份宽度
                .cornerAll(20)     // 圆角弧度
                .marTop(10)        // 顶部距离
                .marLeft(10)       // 左侧距离
                //透明度
                //日期栏0.1f、侧边栏0.1f，周次选择栏0.6f
                //透明度范围为0->1，0为全透明，1为不透明
                //.alpha(0.1f, 0.1f, 0.6f)
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        // 显示课程信息
                        showCourseDetail(scheduleList);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start, int id) {
                        // 显示确认对话框
                        showConfirmDialog(id);
                    }
                })
                .callback(getDateDelayAdapter(startDate))//这行要放在下行的前边
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        if (timetableView.onDateBuildListener() instanceof OnDateDelayAdapter) {
                            OnDateDelayAdapter adapter = (OnDateDelayAdapter) timetableView.onDateBuildListener();
                            long when = adapter.whenBeginSchool();
                            if (when > 0) {
                                titleTextView.setText("距离开学还有" + when + "天");
                            } else {
                                titleTextView.setText("第" + curWeek + "周");
                            }
                        }
                    }
                })
                //旗标布局点击监听
                .callback(new ISchedule.OnFlaglayoutClickListener() {
                    @Override
                    public void onFlaglayoutClick(int day, int start) {
                        timetableView.hideFlaglayout();
                        Intent intent = new Intent(getContext(), AddCourseActivity.class);
                        intent.putExtra("title", "添加课程");
                        intent.putExtra("day", day);
                        intent.putExtra("start", start);
                        startActivity(intent);
                    }
                })
                .showView();
    }


    /**
     * 配置OnDateDelayAdapter
     */
    public OnDateDelayAdapter getDateDelayAdapter(String str_date) {
        OnDateDelayAdapter onDateDelayAdapter = new OnDateDelayAdapter();

        //计算开学时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long startTime = 0;
        Date date = null;
        try {
            startTime = sdf.parse(str_date).getTime();
            date = sdf.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 星期与Date类是同：1代表星期日、2代表星期1、3代表星期二，以此类推
        // 不是星期一就往后面推
        int[] addDays = {1, 0, -1, -2, -3, -4, 2};   // 往后(前)推的天数
        c.add(Calendar.DATE, addDays[c.get(Calendar.DAY_OF_WEEK) - 1]);

        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        Log.e(TAG, "month: " + month);
        Log.e(TAG, "day: " + day);
        // 第一个是月份，后面七个第一周的日期
        //计算开学时的一周日期，我这里模拟一下
        //List<String> dateList = Arrays.asList("9", "3", "4", "5", "6", "7", "8", "9");
        List<String> dateList = new ArrayList<String>();
        dateList.add(String.valueOf(month));
        for (int i = 0; i < 7; i++) {
            dateList.add(String.valueOf(c.get(Calendar.DATE)));
            c.add(Calendar.DATE, 1);
        }
        //设置
        onDateDelayAdapter.setStartTime(startTime);
        onDateDelayAdapter.setDateList(dateList);
        return onDateDelayAdapter;
    }


    /**
     * 周次选择布局的左侧被点击时回调
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String[] items = new String[25];
        int itemCount = weekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, timetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    weekView.curWeek(target + 1).updateView();
                    timetableView.changeWeekForce(target + 1);
                    Date curDate = new Date();
                    String startTime;//(注意！)存放开学日期！形式"yyyy-MM-dd HH:mm:ss"
                    startTime = TimeCalcUtil.date2Str(TimeCalcUtil.calWeeksAgo(curDate, target));
                    myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_CUR_WEEK, startTime);
                    myConfig.saveConfig(myConfigMap);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    /**
     * 显示课程详细信息
     *
     * @param beans 课程列表
     */
    protected void showCourseDetail(List<Schedule> beans) {

        View courseDetail = getLayoutInflater().inflate(R.layout.dialog_course_detail, null);
        RelativeLayout rl_include_detail = courseDetail.findViewById(R.id.include_detail);
        // 课程名
        TextView tv_item = rl_include_detail.findViewById(R.id.tv_item);
        tv_item.setText(beans.get(0).getName());
        // 周数
        TextView et_weeks = rl_include_detail.findViewById(R.id.et_weeks);
        String str_weeks = "第" + beans.get(0).getWeekList().get(0) + "-" + beans.get(0).getWeekList().get(beans.get(0).getWeekList().size() - 1) + "周";
        et_weeks.setText(str_weeks);
        // 节数
        String[] arrayday = {"一", "二", "三", "四", "五", "六", "日"};
        TextView et_time = rl_include_detail.findViewById(R.id.et_time);
        String str_time = "周" + arrayday[beans.get(0).getDay() - 1] + "   第" + beans.get(0).getStart() + "-" + (beans.get(0).getStart() + beans.get(0).getStep() - 1) + "节";
        et_time.setText(str_time);
        // 老师
        EditText et_teacher = rl_include_detail.findViewById(R.id.et_teacher);
        et_teacher.setEnabled(false);
        et_teacher.setText(beans.get(0).getTeacher());
        // 教室
        EditText et_room = rl_include_detail.findViewById(R.id.et_room);
        et_room.setEnabled(false);
        et_room.setText(beans.get(0).getRoom());
        // 设置自定义布局
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(courseDetail);
        final AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  // 背景设置透明
        dialog.getWindow().setLayout(900, WindowManager.LayoutParams.WRAP_CONTENT); // 设置宽高
        // 关闭dialog
        TextView tv_ib_delete = rl_include_detail.findViewById(R.id.ib_delete);
        tv_ib_delete.setClickable(true);
        tv_ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 删除课程
        TextView tv_delete_course = courseDetail.findViewById(R.id.btn_delete_course);
        tv_delete_course.setClickable(true);
        tv_delete_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_tips = courseDetail.findViewById(R.id.tv_tips);
                if (tv_tips.getVisibility() == View.GONE) {
                    tv_tips.setVisibility(View.VISIBLE);
                } else {
                    //  int delete_id = (int)beans.get(0).getExtras().get("extras_id");
                    int delete_id = Integer.parseInt(String.valueOf(beans.get(0).getExtras().get("extras_id")));
                    deleteSubject(delete_id);
                    dialog.dismiss();
                }
            }
        });

        // 编辑课程
        TextView tv_edit_course = courseDetail.findViewById(R.id.btn_edit_course);
        tv_edit_course.setClickable(true);
        tv_edit_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String str = "编辑课程";
//                Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                intent.putExtra("title", "编辑课程");
                intent.putExtra("scheduleList", new Gson().toJson(beans));
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }


    /**
     * 长按显示确认对话框
     *
     * @param id 课程id
     */
    protected void showConfirmDialog(int id) {
        View view = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        TextView text = view.findViewById(R.id.text);
        text.setText("确认删除？");
        TextView confirm = view.findViewById(R.id.confirm);
        TextView cancel = view.findViewById(R.id.cancel);

        // 创建dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(900, WindowManager.LayoutParams.WRAP_CONTENT);

        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteSubject(id);
                 Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 选择开学日期
     * 年，月，日
     */
    protected void selectDate() {
        View selectDateDetail = getLayoutInflater().inflate(R.layout.dialog_select_date, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        initDatePicker(selectDateDetail, c);

        // 设置自定义布局
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(selectDateDetail);
        final AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(900, WindowManager.LayoutParams.WRAP_CONTENT);
        // 关闭dialog
        TextView btn_cancel = selectDateDetail.findViewById(R.id.btn_cancel);
        btn_cancel.setClickable(true);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 保存
        TextView btn_saveDate = selectDateDetail.findViewById(R.id.btn_save_date);
        btn_saveDate.setClickable(true);
        btn_saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
                Log.e(TAG, "savedDate:" + startDate);
                myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_START_DATE, startDate);
                MyConfig.saveConfig(myConfigMap);//保存设置信息至本地配置文件
                Toast.makeText(getContext(), "保存成功！重启生效！", Toast.LENGTH_LONG);
                dialog.dismiss();
            }
        });
    }


    /**
     * 初始化DatePicker
     *
     * @param selectDateDetail DatePickerView
     */
    protected void initDatePicker(View selectDateDetail, Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        // 年
        yearPicker = selectDateDetail.findViewById(R.id.date_year);
        String[] displayYear = {String.valueOf(calendar.get(Calendar.YEAR) - 1), String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.YEAR) + 1)};
        yearPicker.setDisplayedValues(displayYear);
        // 设置最大值
        yearPicker.setMaxValue(displayYear.length - 1);
        // 设置最小值
        yearPicker.setMinValue(0);
        // 设置当前值
        yearPicker.setValue(1);

        // 月
        monthPicker = selectDateDetail.findViewById(R.id.date_month);
        String[] displayMonth = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        monthPicker.setDisplayedValues(displayMonth);
        // 设置最大值
        monthPicker.setMaxValue(displayMonth.length - 1);
        // 设置最小值
        monthPicker.setMinValue(0);
        // 设置当前值
        monthPicker.setValue(month);

        // 日
        dayPicker = selectDateDetail.findViewById(R.id.date_day);
        String[] displayDay = {"1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"};
        dayPicker.setDisplayedValues(displayDay);
        // 设置最大值
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
        // 设置最小值
        dayPicker.setMinValue(0);
        // 设置当前值
        dayPicker.setValue(day - 1);

        // 年改变，同时改变对应月份的天数
        yearPicker.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                calendar.set(Calendar.YEAR, year + newVal - 1);
                dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
                dayPicker.setValue(calendar.get(Calendar.DATE) - 1);
            }
        });
        // 月改变，同时改变对应月份的天数
        monthPicker.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                calendar.set(Calendar.MONTH, newVal);
                if (calendar.get(Calendar.MONTH) != newVal) {
                    calendar.set(Calendar.MONTH, newVal);
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
                dayPicker.setValue(calendar.get(Calendar.DATE) - 1);
            }
        });

        dayPicker.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                calendar.set(Calendar.DATE, newVal + 1);
            }
        });

    }


    /**
     * 删除课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     *
     * @param delete_id 待删除的课程 id
     */
    protected void deleteSubject(int delete_id) {
        // 从 dataSource 删除
        List<Schedule> ds = timetableView.dataSource();
        Iterator<Schedule> it = ds.iterator();
        while (it.hasNext()) {
            Schedule next = it.next();
            // int id = (int) next.getExtras().get("extras_id");
            int id = Integer.parseInt(String.valueOf(next.getExtras().get("extras_id")));
            if (id == delete_id) {
                it.remove();
                break;
            }
        }
        timetableView.updateView();  // 更新视图

        // 从保存的课程中删除
        List<MySubject> ms = mySubjects;
        Iterator<MySubject> iterator = ms.iterator();
        while (iterator.hasNext()) {
            MySubject next = iterator.next();
            int id = next.getId();
            if (id == delete_id) {
                iterator.remove();
                break;
            }
        }
        toSaveSubjects(mySubjects); // 保存课程

    }


    /**
     * 隐藏非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     * updateView()被调用后，会重新构建课程，课程会回到当前周
     */
    protected void hideNonThisWeek() {
        timetableView.isShowNotCurWeek(false).updateView();
    }

    /**
     * 显示非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     */
    protected void showNonThisWeek() {
        timetableView.isShowNotCurWeek(true).updateView();
    }

    /**
     * 显示时间
     * 设置侧边栏构建监听，TimeSlideAdapter是控件实现的可显示时间的侧边栏
     */
    protected void showTime() {
        String[] times = new String[]{
                "8:00", "8:50", "9:50", "10:40",
                "11:30", "14:00", "14:50", "15:50",
                "16:40", "18:30", "19:20", "20:10"
        };
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) timetableView.onSlideBuildListener();
        listener.setTimes(times)
                .setTimeTextColor(Color.BLACK);
        timetableView.updateSlideView();
    }

    /**
     * 隐藏时间
     * 将侧边栏监听置Null后，会默认使用默认的构建方法，即不显示时间
     * 只修改了侧边栏的属性，所以只更新侧边栏即可（性能高），没有必要更新全部（性能低）
     */
    protected void hideTime() {
        timetableView.callback((ISchedule.OnSlideBuildListener) null);
        timetableView.updateSlideView();
    }


    /**
     * 隐藏周次选择，此时需要将课表的日期恢复到本周并将课表切换到当前周
     */
    public void hideWeekView() {
        weekView.isShow(false);
        titleTextView.setTextColor(ContextCompat.getColor(ContextApplication.getAppContext(), R.color.app_course_textcolor_blue));
        int cur = timetableView.curWeek();
        timetableView.onDateBuildListener()
                .onUpdateDate(cur, cur);
        timetableView.changeWeekOnly(cur);
    }

    /**
     * 隐藏WeekView
     */
    public void showWeekView() {
        weekView.isShow(true);
        titleTextView.setTextColor(ContextCompat.getColor(ContextApplication.getAppContext(), R.color.app_red));
    }


    /**
     * 隐藏周末
     */
    private void hideWeekends() {
        timetableView.isShowWeekends(false).updateView();
    }

    /**
     * 显示周末
     */
    private void showWeekends() {
        timetableView.isShowWeekends(true).updateView();
    }

    /**
     * 从本地配置文件中读取信息并应用
     */
    public void loadLocalConfig() {
        //   mMyConfig = new MyConfig(MainActivity.this);
//        mMyConfig = new MyConfig();
        myConfigMap = MyConfig.loadConfig();
        OnMyConfigHandleAdapter onMyConfigHandleAdapter = new OnMyConfigHandleAdapter();
        for (String key : myConfigMap.keySet()) {
            String value = myConfigMap.get(key);

            if (value == null)
                continue;
            switch (key) {
                case OnMyConfigHandleAdapter.CONFIG_SHOW_TIME:
                    if (value.equals(OnMyConfigHandleAdapter.VALUE_TRUE))
                        showTime();
                    else
                        hideTime();
                    break;
                default:
                    onMyConfigHandleAdapter.onParseConfig(key, value, timetableView);
                    break;
            }

        }
        //第一周未设定，将当前周设置为第一周
        if (myConfigMap.get(OnMyConfigHandleAdapter.CONFIG_CUR_WEEK) == null) {
            myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_CUR_WEEK, TimeCalcUtil.date2Str(new Date()));
            myConfig.saveConfig(myConfigMap);
        }
    }


    /**
     * 保存课程
     *
     * @param subject 课程列表
     */
    public static void toSaveSubjects(List<MySubject> subject) {

        Gson gson = new Gson();
        String str_subjectJSON = gson.toJson(subject);
//        SharedPreferences sp = getSharedPreferences("COURSE_DATA", Activity.MODE_PRIVATE);//创建sp对象
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("SUBJECT_LIST", str_subjectJSON); //存入json串
//        editor.commit();//提交
        SharedPreferencesUtil.init(ContextApplication.getAppContext(), "COURSE_DATA").putString("SUBJECT_LIST", str_subjectJSON); //存入json串
        Log.e(TAG, "toSaveSubjects: " + str_subjectJSON);

    }

    /**
     * 获取保存的课程
     *
     * @return 保存的课程列表
     */
    public static List<MySubject> toGetSubjects() {

//        SharedPreferences sp = getSharedPreferences("COURSE_DATA", Activity.MODE_PRIVATE);//创建sp对象
//        String str_subjectJSON = sp.getString("SUBJECT_LIST", null);  //取出key为"SUBJECT_LIST"的值，如果值为空，则将第二个参数作为默认值赋值
//        Log.e(TAG, "toGetSubjects: " + str_subjectJSON);//str_subjectJSON便是取出的数据了
        String str_subjectJSON = SharedPreferencesUtil.init(ContextApplication.getAppContext(), "COURSE_DATA").getString("SUBJECT_LIST", null);
        Gson gson = new Gson();
        List<MySubject> subjectList = gson.fromJson(str_subjectJSON, new TypeToken<List<MySubject>>() {
        }.getType());
        return subjectList;
    }


    private void initNavView() {
        mDrawerLayout = findViewById(R.id.drawyer_layout);
        ImageView b = findViewById(R.id.menu);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navView = findViewById(R.id.nave_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.import_class:
                        Intent intent = new Intent(MainActivity.this, ParseHtmlActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.select_date:
                        selectDate();
                        break;
                    case R.id.hide_not_tish_week:
                        hideNonThisWeek();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_NOT_CUR_WEEK, OnMyConfigHandleAdapter.VALUE_FALSE);
                        break;
                    case R.id.show_not_this_week:
                        showNonThisWeek();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_NOT_CUR_WEEK, OnMyConfigHandleAdapter.VALUE_TRUE);
                        break;
                    case R.id.show_time:
                        showTime();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_TIME, OnMyConfigHandleAdapter.VALUE_TRUE);
                        break;
                    case R.id.hide_time:
                        hideTime();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_TIME, OnMyConfigHandleAdapter.VALUE_FALSE);
                        break;
                    case R.id.hide_weekends:
                        hideWeekends();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_WEEKENDS, OnMyConfigHandleAdapter.VALUE_FALSE);
                        break;
                    case R.id.show_weekends:
                        showWeekends();
                        myConfigMap.put(OnMyConfigHandleAdapter.CONFIG_SHOW_WEEKENDS, OnMyConfigHandleAdapter.VALUE_TRUE);
                        break;
                    case R.id.about_activity:
                        Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intentAbout);
                        break;
                    case R.id.notification_activity:
                        Intent intentNotConfig = new Intent(MainActivity.this, NotificationConfigActivity.class);
                        startActivity(intentNotConfig);
                        break;
                    default:
                        break;
                }
                myConfig.saveConfig(myConfigMap);//保存设置信息至本地配置文件
                return true;
            }
        });
    }


}
