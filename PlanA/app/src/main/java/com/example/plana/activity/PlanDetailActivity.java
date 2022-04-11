package com.example.plana.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;

/**
 * @program: PlanA
 * @description:
 */
public class PlanDetailActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_plan_detail);

    }

}
