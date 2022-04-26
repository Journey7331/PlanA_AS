package com.example.plana.function.timer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.TimerRecorderAdapter;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.TimerRecorder;
import com.example.plana.bean.Todos;
import com.example.plana.database.TimerDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @program: PlanA
 * @description:
 */
public class TimerRecorderActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timer_recorder);

        findViewById(R.id.bt_timer_recorder_back).setOnClickListener(l->{
            finish();
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_record_timer);
        GridLayoutManager layoutManager = new GridLayoutManager(TimerRecorderActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TimerRecorderAdapter(TimerRecorderActivity.this, getRecorderList()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取所有的计时数据
     * */
    private ArrayList<TimerRecorder> getRecorderList() {
        ArrayList<TimerRecorder> records = TimerDB.queryAllTimerRecorder(sqlite);
        // 按添加时间倒序排列
        records.sort((t0, t1) -> t1.get_id() - t0.get_id());
        return records;
    }
}
