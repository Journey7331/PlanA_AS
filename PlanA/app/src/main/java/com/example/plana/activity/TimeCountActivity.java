package com.example.plana.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
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

/**
 * @program: PlanA
 * @description:
 */
public class TimeCountActivity extends BaseActivity {

    TextView tvCountTime, tvCancel, tvStop, tvSec, tvTimeOut, tvOk;
    Boolean threadStart = true;
    private Vibrator mVibrator;

    int remainTime = 0;
    int remainHour = 0;
    int remainMin = 0;
    int remainSec = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        // Clean Status Bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

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

        tvCancel.setOnClickListener(l -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", 4);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_out_top, R.anim.slide_out_bottom);
            startActivity(intent, options.toBundle());
            finish();
        });

        tvStop.setOnClickListener(l -> {
            if (threadStart) {
                mHandler.removeCallbacks(runnable);
                tvStop.setText("Start");
                threadStart = false;
            } else {
                mHandler.post(runnable);
                tvStop.setText("Stop");
                threadStart = true;
            }
        });

        tvOk.setOnClickListener(l -> {
            tvCancel.performClick();
        });

    }

    public void reFreshTime() {
        // Time Out
        if (remainTime == 0) {
            mHandler.removeCallbacks(runnable);
            mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(2000);
            tvCountTime.setText("Done");
            tvCancel.setText("");
            tvStop.setText("");
            tvTimeOut.setText(">>> Take Some Rest <<<");
            tvOk.setText("OK");
        }
        remainHour = remainTime / 60 / 60;
        remainMin = (remainTime / 60) % 60;
        remainSec = remainTime % 60;
        if (remainTime >= 60 * 60) {
            tvSec.setText(String.format("%02d", remainSec));
        } else {
            tvSec.setText("");
        }
        if (remainHour != 0) {
            tvCountTime.setText(String.format("%02d", remainHour) + ":" + String.format("%02d", remainMin));
        } else {
            tvCountTime.setText(String.format("%02d", remainMin) + ":" + String.format("%02d", remainSec));
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

}


