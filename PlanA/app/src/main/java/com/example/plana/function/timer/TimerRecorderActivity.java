package com.example.plana.function.timer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.TimerRecorderAdapter;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.TimerRecorder;

import java.util.ArrayList;

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

        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_record_timer);
        GridLayoutManager layoutManager = new GridLayoutManager(TimerRecorderActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TimerRecorderAdapter(TimerRecorderActivity.this, getRecorderList()));

        // TODO: change the  ItemDecoration
        //        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                outRect.top = 20;
//                outRect.bottom = 0;
//            }
//        });

    }

    /**
     * 获取所有的计时数据
     * */
    private ArrayList<TimerRecorder> getRecorderList() {
        ArrayList<TimerRecorder> list = new ArrayList<>();

        list.add(new TimerRecorder("22.04.09", 9, 32, 25));
        list.add(new TimerRecorder("22.04.03", 17, 50, 20));
        list.add(new TimerRecorder("22.03.28", 23, 32, 111));

        return list;

    }
}
