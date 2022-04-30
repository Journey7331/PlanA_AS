package com.example.plana.function;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.function.fragment.FocusFragment;
import com.example.plana.function.fragment.MyPageFragment;
import com.example.plana.function.fragment.PlanFragment;
import com.example.plana.function.fragment.ScheduleFragment;
import com.example.plana.function.fragment.ShowListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @program: PlanA
 * @description:
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    public static MainActivity mainActivity;

    BottomNavigationView bottomNavigation;

    ShowListFragment listFragment;
    ScheduleFragment scheduleFragment;
    PlanFragment planFragment;
    FocusFragment focusFragment;
    MyPageFragment myPageFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        hideKeyboard(this);

        // fragment setup
        listFragment = new ShowListFragment();
        scheduleFragment = new ScheduleFragment();
        planFragment = new PlanFragment();
        focusFragment = new FocusFragment();
        myPageFragment = new MyPageFragment();
        currentFragment = null;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, listFragment)
                .commit();

        // bottom navigation bar
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationSelectedListener);

        if (My.page > 0) {
            bottomNavigation.setSelectedItemId(My.page);
            My.page = -1;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            mainActivity.bottomNavigation.setSelectedItemId(R.id.page_3);
        }
    }

    @Override
    protected void onResume() {
        getNotify(getIntent());
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNotify(intent);
        setIntent(intent);
    }

    private void getNotify(Intent intent) {
        String value = intent.getStringExtra("SCHEDULE_NOTIFY");
        Log.i("TAG", "onNewIntent: " + value);

        if (value != null && value.equals("SCHEDULE_NOTIFY")) {
            bottomNavigation.setSelectedItemId(R.id.page_2);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.page_1:
                onFragmentSelected(1, null);
                break;
            case R.id.page_2:
                onFragmentSelected(2, null);
                break;
            case R.id.page_3:
                onFragmentSelected(3, null);
                break;
            case R.id.page_4:
                onFragmentSelected(4, null);
                break;
            case R.id.page_5:
                onFragmentSelected(5, null);
                break;
        }
        return true;
    };

    public void onFragmentSelected(int position, Bundle bundle) {
        switch (position) {
            case 1:
                listFragment = new ShowListFragment();
                currentFragment = listFragment;
                break;
            case 2:
                scheduleFragment = new ScheduleFragment();
                currentFragment = scheduleFragment;
                break;
            case 3:
                planFragment = new PlanFragment();
                currentFragment = planFragment;
                break;
            case 4:
                focusFragment = new FocusFragment();
                currentFragment = focusFragment;
                break;
            case 5:
                myPageFragment = new MyPageFragment();
                currentFragment = myPageFragment;
                break;

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, currentFragment)
                .commit();
    }
}