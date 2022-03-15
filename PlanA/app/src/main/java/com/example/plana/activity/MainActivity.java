package com.example.plana.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.fragment.FocusFragment;
import com.example.plana.fragment.MyPageFragment;
import com.example.plana.fragment.ScheduleFragment;
import com.example.plana.fragment.ShowListFragment;
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

    FocusFragment focusFragment;
    MyPageFragment myPageFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        // fragment setup
        listFragment = new ShowListFragment();
//        scheduleFragment = new ScheduleFragment();
//        focusFragment = new FocusFragment();
//        myPageFragment = new MyPageFragment();
        currentFragment = null;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, listFragment)
                .commit();

        // bottom navigation bar
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationSelectedListener);

        // do what?
        if (getIntent().getIntExtra("page", 0) == 4) {
            bottomNavigation.setSelectedItemId(R.id.page_4);
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
        hideKeyboard(this);
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
                Toast.makeText(this, "will be changed into schedule...", Toast.LENGTH_SHORT).show();
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