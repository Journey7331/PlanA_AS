package com.example.plana.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.plana.activity.AddCourseActivity;
import com.example.plana.activity.ScheduleSettingActivity;
import com.example.plana.bean.My;
import com.example.plana.config.Constant;
import com.example.plana.config.MyConfig;
import com.example.plana.R;
import com.example.plana.activity.MainActivity;
import com.example.plana.adapter.OnDateDelayAdapter;
import com.example.plana.config.MyConfigConstant;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.MySubject;
import com.example.plana.utils.ContextApplication;
import com.example.plana.utils.SharedPreferencesUtil;
import com.example.plana.utils.SubjectRepertory;
import com.example.plana.utils.TimeCalcUtil;
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
    ImageView ivScheduleSetting;

    int target = -1;     // 记录切换的周次，不一定是当前周
    String startDate = "2022-2-27"; // 开学时间

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        titleTextView = view.findViewById(R.id.id_title);
        layout = view.findViewById(R.id.id_layout);
        ivScheduleSetting = view.findViewById(R.id.schedule_setting);
        layout.setOnClickListener(this);
        ivScheduleSetting.setOnClickListener(this);

        startDate = SharedPreferencesUtil.init(ContextApplication.getAppContext(), CONFIG_FILENAME)
                .getString(
                        MyConfigConstant.CONFIG_START_DATE,
                        startDate
                );
//        Log.d(TAG, startDate);

        loadSubjects();     // 加载课程
        initTimetableView(view);  // 初始化界面
        loadLocalConfig();    // 读取本地配置文件

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        myConfigMap = MyConfig.loadConfig();

        //用于更正日期和weekView的显示
        int cur = timetableView.curWeek();
        timetableView.onDateBuildListener().onUpdateDate(cur, cur);
        weekView.curWeek(cur).updateView();

        //更新文本
        OnDateDelayAdapter adapter = (OnDateDelayAdapter) timetableView.onDateBuildListener();
        long when = adapter.whenBeginSchool();
        if (when > 0) {
            String str = "距离开学还有" + when + "天";
            titleTextView.setText(str);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        loadSubjects();     // 加载课程
        loadLocalConfig();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                // 查看，关闭周次
                if (weekView.isShowing()) hideWeekView();
                else showWeekView();
                break;
            case R.id.schedule_setting:
                Intent intent = new Intent(MainActivity.mainActivity, ScheduleSettingActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 载入课程数据到list中
     */
    private void loadSubjects() {
        String subjectListJson = SharedPreferencesUtil.init(ContextApplication.getAppContext(), "COURSE_DATA").getString("SUBJECT_LIST", null);
        if (subjectListJson == null) {
            mySubjects = SubjectRepertory.loadDefaultSubjects();
            if (!mySubjects.isEmpty()) {
                toSaveSubjects(mySubjects);
            }
        } else {
            mySubjects = toGetSubjects();
        }
        My.mySubjects = mySubjects;
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
//                .curWeek(1)
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
                .isShow(false)
                .showView();

        timetableView.source(mySubjects)  // 课程源
//                .curWeek(week)
                .curTerm(null)     // 当前学期
                .maxSlideItem(12)  // 节数
                .monthWidthDp(40)  // 月份宽度
                .cornerAll(20)     // 圆角弧度
                .marTop(10)        // 顶部距离
                .marLeft(10)       // 左侧距离
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
        //计算开学时的一周日期，这里模拟一下
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
        target = timetableView.curWeek() - 1;
        AlertDialog alertDialog = new AlertDialog
                .Builder(getContext())
                .setTitle("设置当前周")
                .setSingleChoiceItems(
                        items,
                        timetableView.curWeek() - 1,
                        (dialogInterface, i) -> target = i
                )
                .setPositiveButton("设置为当前周", (dialog, which) -> {
                    weekView.curWeek(target + 1).updateView();
                    weekView.smoothScrollToCurWeek();
                    timetableView.changeWeekForce(target + 1);
                    Date curDate = new Date();
                    String startTime;//(注意！)存放开学日期！形式"yyyy-MM-dd HH:mm:ss"
                    startTime = TimeCalcUtil.date2Str(TimeCalcUtil.calWeeksAgo(curDate, target));
                    myConfigMap.put(MyConfigConstant.CONFIG_CUR_WEEK, startTime);
                    MyConfig.saveConfig(myConfigMap);
                })
                .setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.ColorBlueGrey);
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.ColorBlueGrey);
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
        tv_ib_delete.setOnClickListener(v -> dialog.dismiss());

        // 删除课程
        ImageButton bt_delete_course = courseDetail.findViewById(R.id.btn_delete_course);
        bt_delete_course.setClickable(true);
        bt_delete_course.setOnClickListener(v -> {
            TextView tv_tips = courseDetail.findViewById(R.id.tv_tips);
            if (tv_tips.getVisibility() == View.GONE) {
                tv_tips.setVisibility(View.VISIBLE);
            } else {
                //  int delete_id = (int)beans.get(0).getExtras().get("extras_id");
                int delete_id = Integer.parseInt(String.valueOf(beans.get(0).getExtras().get("extras_id")));
                deleteSubject(delete_id);
                dialog.dismiss();
            }
        });

        // 编辑课程
        ImageButton bt_edit_course = courseDetail.findViewById(R.id.btn_edit_course);
        bt_edit_course.setClickable(true);
        bt_edit_course.setOnClickListener(v -> {
//                String str = "编辑课程";
//                Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.mainActivity, AddCourseActivity.class);
            intent.putExtra("title", "编辑课程");
            intent.putExtra("scheduleList", new Gson().toJson(beans));
            startActivity(intent);
            dialog.dismiss();
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
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            deleteSubject(id);
            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
        });
        // 取消
        cancel.setOnClickListener(v -> dialog.dismiss());
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
        titleTextView.setTextColor(Constant.ColorBlueGrey);
        int cur = timetableView.curWeek();
        timetableView.onDateBuildListener()
                .onUpdateDate(cur, cur);
        timetableView.changeWeekOnly(cur);
    }

    /**
     * 显示WeekView
     */
    public void showWeekView() {
        weekView.isShow(true);
        titleTextView.setTextColor(Constant.ColorMyRed);
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
     * 从本地配置文件中读取信息并应用
     */
    public void loadLocalConfig() {
        myConfigMap = MyConfig.loadConfig();
        for (String key : myConfigMap.keySet()) {
            String value = myConfigMap.get(key);
            if (value == null)
                continue;
            switch (key) {
                case MyConfigConstant.CONFIG_SHOW_TIME:
                    if (value.equals(MyConfigConstant.VALUE_TRUE)) showTime();
                    else hideTime();
                    break;
                case MyConfigConstant.CONFIG_SHOW_WEEKENDS:
                    if (value.equals(MyConfigConstant.VALUE_TRUE)) showWeekends();
                    else hideWeekends();
                    break;
                case MyConfigConstant.CONFIG_SHOW_NOT_CUR_WEEK:
                    if (value.equals(MyConfigConstant.VALUE_TRUE)) showNonThisWeek();
                    else hideNonThisWeek();
                    break;
                case MyConfigConstant.CONFIG_CUR_WEEK:
                    timetableView.curWeek(value);
                    weekView.curWeek(timetableView.curWeek());
                    break;
            }

        }
        //第一周未设定，将当前周设置为第一周
        if (myConfigMap.get(MyConfigConstant.CONFIG_CUR_WEEK) == null) {
            myConfigMap.put(MyConfigConstant.CONFIG_CUR_WEEK, TimeCalcUtil.date2Str(new Date()));
            MyConfig.saveConfig(myConfigMap);
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
        My.mySubjects = subject;

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


}
