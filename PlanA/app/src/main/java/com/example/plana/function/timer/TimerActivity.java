package com.example.plana.function.timer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.TimerRecorder;
import com.example.plana.config.Constant;
import com.example.plana.config.MyConfig;
import com.example.plana.config.MyConfigConstant;
import com.example.plana.database.TimerDB;
import com.example.plana.function.schedule.ScheduleSettingActivity;
import com.example.plana.utils.TimeCalcUtil;

import java.util.Calendar;

/**
 * @program: PlanA
 * @description:
 */
public class TimerActivity extends BaseActivity {

    TextView tvCountTime, tvCancel, tvStop, tvSec, tvTimeOut, tvOk;
    Boolean threadStart = true;
    String count_type;
    Runnable runnable;  // 当前运行的 runnable

    String day;
    String startTime;
    int startHour, startMin;
    int time;

    int remainTime = 0;
    int remainHour = 0;
    int remainMin = 0;
    int remainSec = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);
        cleanStatusBar();

        tvCountTime = findViewById(R.id.tv_count_time_show);
        tvSec = findViewById(R.id.tv_sec);
        tvCancel = findViewById(R.id.tv_cancel);
        tvStop = findViewById(R.id.tv_stop);
        tvTimeOut = findViewById(R.id.tv_time_out);
        tvOk = findViewById(R.id.tv_ok);

        count_type = getIntent().getStringExtra("count_type");
        Calendar cal = Calendar.getInstance();
        day = TimeCalcUtil.calToSimpleStr(cal);
        startHour = cal.get(Calendar.HOUR_OF_DAY);
        startMin = cal.get(Calendar.MINUTE);
        startTime = TimeCalcUtil.format02d(startHour, startMin);

        if (count_type.equals(TimerRecorder.NEGATIVE)) {
            time = getIntent().getIntExtra("time", 0);
            runnable = negativeRunnable;
            remainTime = 60 * time;
        } else {
            tvCancel.setText("结束");
            runnable = positiveRunnable;
            remainTime = 0;
        }
        reFreshTime();
        mHandler.post(runnable);
        tvCancel.setOnClickListener(l -> {
            showConformDialog();
        });

        tvStop.setOnClickListener(l -> {
            if (threadStart) {
                mHandler.removeCallbacks(runnable);
                tvStop.setText("开始");
                threadStart = false;
            } else {
                mHandler.post(runnable);
                tvStop.setText("暂停");
                threadStart = true;
            }
        });

        tvOk.setOnClickListener(l -> {
            finish();
        });

    }


    /**
     * 取消状态栏，沉浸式
     */
    private void cleanStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 取消的二次确认
     */
    private void showConformDialog() {
        Boolean isCounting = false;
        // 如果还在计时中，先暂停
        if (threadStart) {
            isCounting = true;
            mHandler.removeCallbacks(runnable);
            threadStart = false;
        }

        View view = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        TextView text = view.findViewById(R.id.text);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView cancel = view.findViewById(R.id.cancel);

        // 倒计时取消
        if (count_type.equals(TimerRecorder.NEGATIVE)) {
            Boolean finalIsCounting = isCounting;
            text.setText("取消本次专注？");
            confirm.setText("继续计时");
            cancel.setText("取消");

            AlertDialog.Builder builder = new AlertDialog
                    .Builder(TimerActivity.this)
                    .setView(view);
            final AlertDialog dialog = builder.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(700, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (finalIsCounting) {
                        mHandler.post(runnable);
                        threadStart = true;
                    }
                }
            });

            // 继续计时
            confirm.setOnClickListener(v -> {
                if (finalIsCounting) {
                    mHandler.post(runnable);
                    threadStart = true;
                }else {
                    tvStop.performClick();
                }
                dialog.dismiss();
            });
            // 取消
            cancel.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

        if (count_type.equals(TimerRecorder.POSITIVE)) {
            Boolean finalIsCounting = isCounting;
            text.setText("已经完成本次专注？");
            confirm.setText("确认完成");
            cancel.setText("不记录本次");

            // 创建dialog
            AlertDialog.Builder builder = new AlertDialog
                    .Builder(TimerActivity.this)
                    .setView(view);
            final AlertDialog dialog = builder.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(700, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    if (finalIsCounting) {
                        mHandler.post(runnable);
                        threadStart = true;
                    }
                }
            });

            // 确认计入
            confirm.setOnClickListener(v -> {
                time = remainTime / 60;
                if (time < 1) {
                    Toast.makeText(TimerActivity.this,"小于一分钟的专注不记录",Toast.LENGTH_SHORT).show();
                }else {
                    saveRecorder();
                    Toast.makeText(TimerActivity.this, "计入成功", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                finish();
            });
            // 不记录本次
            cancel.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

    }

    /**
     * 防止误触返回键提前中断
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (count_type.equals(TimerRecorder.NEGATIVE) && remainTime == 0) {
                finish();
            } else {
                tvCancel.performClick();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 刷新时间
     */
    public void reFreshTime() {
        if (count_type.equals(TimerRecorder.NEGATIVE) && remainTime == 0) {
            mHandler.removeCallbacks(runnable);
            Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(1500);
            tvCountTime.setText("完成");
            tvCancel.setText("");
            tvStop.setText("");
            tvTimeOut.setText("休息一会儿吧～");
            tvOk.setText("确认");

            saveRecorder();
        }

        remainHour = remainTime / 60 / 60;
        remainMin = (remainTime / 60) % 60;
        remainSec = remainTime % 60;
        if (remainTime >= 60 * 60) {
            tvSec.setText(TimeCalcUtil.format02d(remainSec));
        } else {
            tvSec.setText("");
        }
        if (remainHour != 0) {
            tvCountTime.setText(TimeCalcUtil.format02d(remainHour, remainMin));
        } else {
            tvCountTime.setText(TimeCalcUtil.format02d(remainMin, remainSec));
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                reFreshTime();
            }
        }
    };

    /**
     * 倒计时线程
     */
    private Runnable negativeRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(negativeRunnable, 1000);
            remainTime--;
            reFreshTime();
        }
    };

    /**
     * 正计时线程
     */
    private Runnable positiveRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(positiveRunnable, 1000);
            remainTime++;
            reFreshTime();
        }
    };

    /**
     * 保存本次计时记录
     */
    private void saveRecorder() {
        ContentValues values = new ContentValues();
        values.put(TimerDB.day, day);
        values.put(TimerDB.start_time, startTime);
        values.put(TimerDB.end_time, TimeCalcUtil.calcEndTime(startHour, startMin, time));
        values.put(TimerDB.time, time);
        values.put(TimerDB.count_type, count_type);
//        values.put(TimerDB.tag, tag);

        TimerDB.insertTimerRecorder(sqlite, values);
    }

}


