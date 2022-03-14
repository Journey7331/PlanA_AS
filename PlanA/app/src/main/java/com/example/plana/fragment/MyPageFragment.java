package com.example.plana.fragment;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.bean.User;
import com.example.plana.database.TodosDB;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.UserDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */

public class MyPageFragment extends BaseFragment
        implements View.OnClickListener {

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

        pgLevel = view.findViewById(R.id.pg_level);
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
        // Get Events
        MyDatabaseHelper mysql = new MyDatabaseHelper(getContext());

        // Set User Information
        if (!"".equals(My.Account.getPhone())) {
            myName.setText(My.Account.getName());
            if ("".equals(My.Account.getEmail())) {
                myEmail.setText(My.Account.getPhone());
            } else {
                myEmail.setText(My.Account.getEmail());
            }
            btLogOut.setOnClickListener(l -> {
                ContentValues values = new ContentValues();
                values.put(UserDB.phone, "");
                UserDB.updateUser(new MyDatabaseHelper(getContext()), 0, values);
                startActivity(new Intent(getContext(), LoginActivity.class));
                // 防止 返回键可以重新回到MyPage
                getActivity().finish();
                Log.i("Logout", "** Log out, Logged Phone Cleaned");
                Toast.makeText(getContext(), "Log Out", Toast.LENGTH_SHORT).show();
            });
        } else {
            btLogOut.setText("Log In");
            btLogOut.setOnClickListener(l -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                // 防止 返回键可以重新回到MyPage
                getActivity().finish();
                Log.i("Login", "** Go to Log in Page");
            });
        }

        if (My.todos.size() == 0) {
            pgLevel.setProgress(0);
            tvLevelCount.setText("");
            return;
        }

        // Count Done
        for (Todos e : My.todos) if (e.isDone()) doneCount++;

        unDoneCount = My.todos.size() - doneCount;
        if (doneCount > 0) {
            tvDoneCount.setText(doneCount + "");
        }
        if (unDoneCount > 0) {
            tvUndoneCount.setText(unDoneCount + "");
        }

        // Set ProgressBar
        int percent = doneCount * 100 / My.todos.size();
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
            // TODO Go to Undone Page
            //  Error
//            ((SwitchCompat) getActivity().findViewById(R.id.switch_done)).setChecked(false);
        }

    }
}
