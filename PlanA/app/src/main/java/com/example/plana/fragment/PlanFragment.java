package com.example.plana.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji2.widget.EmojiButton;
import androidx.emoji2.widget.EmojiTextView;

import com.example.plana.R;
import com.example.plana.activity.AchievementActivity;
import com.example.plana.activity.AddPlanActivity;
import com.example.plana.adapter.OnDateDelayAdapter;
import com.example.plana.base.BaseFragment;
import com.example.plana.config.MyConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @program: PlanA
 * @description:
 */
public class PlanFragment extends BaseFragment {

    public static final String TAG = "PlanFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);

        EmojiTextView tvPlanTitle = view.findViewById(R.id.tv_plan_list_title);
        EmojiButton btAchievement = view.findViewById(R.id.bt_my_achievement);
        FloatingActionButton fab = view.findViewById(R.id.add_plan_fab);

        RelativeLayout rlPlan1 = view.findViewById(R.id.plan_brief_1);
        ProgressBar progressBar1 = rlPlan1.findViewById(R.id.pg_level);
        TextView planName1 =  rlPlan1.findViewById(R.id.tv_plan_name);
        planName1.setText("JAVA基础");

        RelativeLayout rlPlan2 = view.findViewById(R.id.plan_brief_2);
        ProgressBar progressBar2 = rlPlan2.findViewById(R.id.pg_level);
        TextView planName2 =  rlPlan2.findViewById(R.id.tv_plan_name);
        planName2.setText("Python基础");


        new Handler().postDelayed(() -> {
            progressBar1.setProgress(30, true);
            progressBar2.setProgress(70, true);
        }, 500);

        tvPlanTitle.append("  做个计划吧~");

        btAchievement.setOnClickListener(l->{
            startActivity(new Intent(getContext(), AchievementActivity.class));
        });

        fab.setOnClickListener(l->{
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






}
