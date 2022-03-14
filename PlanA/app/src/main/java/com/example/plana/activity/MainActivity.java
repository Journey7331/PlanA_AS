package com.example.plana.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.User;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.UserDB;
import com.example.plana.fragment.AddFragment;
import com.example.plana.fragment.FocusFragment;
import com.example.plana.fragment.MyPageFragment;
import com.example.plana.fragment.ShowListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @program: PlanA
 * @description:
 */
public class MainActivity extends BaseActivity {

    BottomNavigationView bottomNavigation;

    AddFragment addFragment;
    ShowListFragment listFragment;
    FocusFragment focusFragment;
    MyPageFragment myPageFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check login status

        String phone = UserDB.getPhone(mysql);
        if ("".equals(phone)) {
            this.user = new User(0, "", "");
        } else {
            this.user = UserDB.getUser(mysql, phone);
        }

        // fragment setup
        addFragment = new AddFragment();
        listFragment = new ShowListFragment(user.getName());
        focusFragment = new FocusFragment();
        myPageFragment = new MyPageFragment(this.user);
        currentFragment = null;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, homeFragment)
                .commit();

        // bottom navigation bar
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationSelectedListener);

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
                homeFragment = new HomeFragment(this.user.getName());
                currentFragment = homeFragment;
                break;
            case 2:
                Toast.makeText(this, "MapFragment is developing...", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                addFragment = new AddFragment();
                currentFragment = addFragment;
                break;
            case 4:
                focusFragment = new FocusFragment();
                currentFragment = focusFragment;
                break;
            case 5:
                myPageFragment = new MyPageFragment(user);
                currentFragment = myPageFragment;
                break;

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_rl, currentFragment)
                .commit();
    }
}