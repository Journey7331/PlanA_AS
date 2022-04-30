package com.example.plana.function.setting;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;

/**
 * @program: PlanA
 * @description:
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button btBack = findViewById(R.id.bt_about_back);

        btBack.setOnClickListener(l->{
            finish();
        });
    }
}
