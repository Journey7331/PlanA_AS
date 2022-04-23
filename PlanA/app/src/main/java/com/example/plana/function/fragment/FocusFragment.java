package com.example.plana.function.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.function.timer.TimerActivity;
import com.example.plana.function.timer.TimerRecorderActivity;
import com.example.plana.base.BaseFragment;
import com.example.plana.config.Constant;

/**
 * @program: PlanA
 * @description:
 */
public class FocusFragment extends BaseFragment
        implements View.OnClickListener {

    TextView tvTimerName;
    EditText etFocusTime;
    ImageView btRecorder, btTimer;
    Button btUp, btDown, btSubmit;
    ProgressBar pgTime;

    int index = 3;
    final int[] times = {1, 5, 10, 25, 30, 45, 60, 90, 120};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus_set_time, container, false);

        // TODO Add Pop Time-Picker
        //  Personally input todos
        etFocusTime = view.findViewById(R.id.et_focus_time);
        tvTimerName = view.findViewById(R.id.tv_timer_name);

        btUp = view.findViewById(R.id.bt_focus_time_up);
        btDown = view.findViewById(R.id.bt_focus_time_down);
        btSubmit = view.findViewById(R.id.bt_focus_submit);

        btRecorder = view.findViewById(R.id.bt_timer_recorder);
        btTimer = view.findViewById(R.id.bt_timer_selector);
        pgTime = view.findViewById(R.id.count_down_progressbar);

//        etFocusTime.setOnClickListener(this);
        btUp.setOnClickListener(this);
        btDown.setOnClickListener(this);
        btRecorder.setOnClickListener(this);

        btTimer.setOnClickListener(l -> {
            if (btUp.getVisibility() == View.VISIBLE) {
                tvTimerName.setText("正计时");
                pgTime.setProgress(0, true);
                etFocusTime.setText("00:00");
                btUp.setVisibility(View.INVISIBLE);
                btDown.setVisibility(View.INVISIBLE);
            } else {
                tvTimerName.setText("倒计时");
                pgTime.setProgress(times[index], true);
                etFocusTime.setText(times[index] + ":00");
                btUp.setVisibility(View.VISIBLE);
                btDown.setVisibility(View.VISIBLE);
            }
        });

        btSubmit.setOnClickListener(l -> {
            Intent intent = new Intent(getContext(), TimerActivity.class);
            intent.putExtra("time", times[index]);
            intent.putExtra("type", Constant.Timer.NEGATIVE);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_top, R.anim.slide_in_bottom);
            startActivity(intent, options.toBundle());
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_timer_recorder) {
            Intent intent = new Intent(getContext(), TimerRecorderActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.bt_focus_time_up) {
            if (index < times.length - 1) {
                pgTime.setProgress(times[++index], true);
                etFocusTime.setText(times[index] + ":00");
            }
        } else if (v.getId() == R.id.bt_focus_time_down) {
            if (index > 0) {
                pgTime.setProgress(times[--index], true);
                etFocusTime.setText(times[index] + ":00");
            }
        }
    }
}