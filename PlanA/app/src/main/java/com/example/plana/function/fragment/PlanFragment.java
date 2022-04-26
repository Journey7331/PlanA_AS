package com.example.plana.function.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji2.widget.EmojiButton;
import androidx.emoji2.widget.EmojiTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plana.R;
import com.example.plana.adapter.PlanBriefAdapter;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.PlanBrief;
import com.example.plana.function.plan.AchievementActivity;
import com.example.plana.function.plan.AddPlanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

/**
 * @program: PlanA
 * @description:
 */
public class PlanFragment extends BaseFragment {

    public static final String TAG = "PlanFragment";

    TextView tvPlanTitle;
    RecyclerView recyclerView;

    EmojiButton btAchievement;
    FloatingActionButton fab;

    // 卡片之间的间距
    int space = 30;

    // GridView 的列数
    int column = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        tvPlanTitle = view.findViewById(R.id.tv_plan_list_title);
        recyclerView = view.findViewById(R.id.plans_recycler_view);
        tvPlanTitle.append("  做个计划吧~");

        initRecyclerView();

        view.findViewById(R.id.bt_my_achievement).setOnClickListener(l -> {
            startActivity(new Intent(getContext(), AchievementActivity.class));
        });

        view.findViewById(R.id.add_plan_fab).setOnClickListener(l -> {
            startActivity(new Intent(getContext(), AddPlanActivity.class));
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 设置 RecyclerView 的样式
     * 填充 list 内容
     */
    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PlanBriefAdapter(getActivity(), getPlanBriefList()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // 每个span分配的间隔大小
                int spanSpace = space * (column + 1) / column;
                // 列索引
                int colIndex = position % column;
                // 列左、右间隙
                outRect.left = space * (colIndex + 1) - spanSpace * colIndex;
                outRect.right = spanSpace * (colIndex + 1) - space * (colIndex + 1);
                outRect.top = space;
            }
        });
    }


    /**
     * 获取Plan的简介列表
     */
    private LinkedList<PlanBrief> getPlanBriefList() {
        LinkedList<PlanBrief> list = new LinkedList<>();

        list.add(new PlanBrief("JAVA基础", 70, 20, 15));
        list.add(new PlanBrief("Python基础", 50, 20, 7));
        list.add(new PlanBrief("Redis基础", 93, 45, 20));

        return list;
    }


}
