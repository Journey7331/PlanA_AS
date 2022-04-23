package com.example.plana.function.timer;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.utils.TimeCalcUtil;

/**
 * @program: PlanA
 * @description:
 */
public class TimerActivity extends BaseActivity {

    TextView tvCountTime, tvCancel, tvStop, tvSec, tvTimeOut, tvOk;
    Boolean threadStart = true;

    int remainTime = 0;
    int remainHour = 0;
    int remainMin = 0;
    int remainSec = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cleanStatusBar();

        setContentView(R.layout.activity_time_count);

        tvCountTime = findViewById(R.id.tv_count_time_show);
        tvSec = findViewById(R.id.tv_sec);
        tvCancel = findViewById(R.id.tv_cancel);
        tvStop = findViewById(R.id.tv_stop);
        tvTimeOut = findViewById(R.id.tv_time_out);
        tvOk = findViewById(R.id.tv_ok);

        remainTime = getIntent().getIntExtra("time", 0);
        remainTime *= 60;
        reFreshTime();
        mHandler.post(runnable);

        tvCancel.setOnClickListener(l -> showConformDialog());

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
            tvCancel.performClick();
        });

    }


    /**
     * 取消状态栏，沉浸式
     * */
    private void cleanStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 取消的二次确认
     * */
    private void showConformDialog() {

        // TODO show Conform Dialog

        finish();
    }

    /**
     * 刷新时间
     * */
    public void reFreshTime() {
        // Time Out
        if (remainTime == 0) {
            mHandler.removeCallbacks(runnable);
            Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(2000);
            tvCountTime.setText("完成");
            tvCancel.setText("");
            tvStop.setText("");
            tvTimeOut.setText("休息一会儿把");
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
            switch (msg.what) {
                case 0:
                    reFreshTime();
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable, 1000);
            remainTime--;
            reFreshTime();
        }
    };



    private void saveRecorder() {
        // TODO insert Recorder
//        TimerDB.insert(sqlite, )

    }

}


