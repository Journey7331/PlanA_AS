package com.example.plana.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.activity.LoginActivity;
import com.example.plana.activity.MainActivity;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @program: PlanA
 * @description:
 */

public class MyPageFragment extends BaseFragment
        implements View.OnClickListener {

    public static final String TAG = "MyPageFragment";

    TextView myName, myEmail, tvLevelCount, tvUndoneCount, tvDoneCount;
    RelativeLayout rlUndone, rlDone, rlGoals;
    View viewSetting;

    ProgressBar pgLevel;
    Button btLogOut;
    int doneCount, unDoneCount;

    public MyPageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        myName = view.findViewById(R.id.my_name);
        myEmail = view.findViewById(R.id.my_email);
        btLogOut = view.findViewById(R.id.btn_logout);
        viewSetting = view.findViewById(R.id.my_setting);
        rlGoals = view.findViewById(R.id.goals_rl);
        rlUndone = view.findViewById(R.id.undone_rl);
        rlDone = view.findViewById(R.id.done_rl);

        pgLevel = view.findViewById(R.id.progressbar_plan_brief);
        tvLevelCount = view.findViewById(R.id.tv_level_count);
        tvUndoneCount = view.findViewById(R.id.tv_undone_count);
        tvDoneCount = view.findViewById(R.id.tv_done_count);

        myPageSetUp();

        viewSetting.setOnClickListener(this);
        rlGoals.setOnClickListener(this);
        rlUndone.setOnClickListener(this);
        rlDone.setOnClickListener(this);

        return view;
    }

    private void myPageSetUp() {

        // Set User Information
        if (My.Account != null) {
            myName.setText(My.Account.getName());
            if ("".equals(My.Account.getEmail())) {
                myEmail.setText(My.Account.getPhone());
            } else {
                myEmail.setText(My.Account.getEmail());
            }
            btLogOut.setOnClickListener(l -> {
                Toast.makeText(
                        getContext(),
                        "登出",
                        Toast.LENGTH_SHORT
                ).show();
                My.Account = null;
                startActivity(new Intent(getContext(), LoginActivity.class));
                MainActivity.mainActivity.finish();

                SharedPreferences auto = MainActivity.mainActivity.getSharedPreferences("auto", Context.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.clear();
                autoLogin.apply();

                Log.d(TAG, "登出，删除 auto SharedPreferences ");
            });
        } else {
            btLogOut.setText("登录");
            btLogOut.setOnClickListener(l -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                Log.d(TAG, "进入登录页");
            });
        }

        if (My.todosList.size() == 0) {
            pgLevel.setProgress(0);
            tvLevelCount.setText("");
            return;
        }

        // Count Done
        for (Todos e : My.todosList) if (e.isDone()) doneCount++;

        unDoneCount = My.todosList.size() - doneCount;
        if (doneCount > 0) {
            tvDoneCount.setText(doneCount + "");
        }
        if (unDoneCount > 0) {
            tvUndoneCount.setText(unDoneCount + "");
        }

        // Set ProgressBar
        int percent = doneCount * 100 / My.todosList.size();
        pgLevel.setProgress(percent);
        pgLevel.setMax(100);
        tvLevelCount.setText(percent + "%");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.my_setting) {
            Toast.makeText(getContext(), "Setting Functions is developing...", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.goals_rl || v.getId() == R.id.undone_rl || v.getId() == R.id.done_rl) {
            ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.page_1);
        }

    }
}
