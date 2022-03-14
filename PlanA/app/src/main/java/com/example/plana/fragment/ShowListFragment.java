package com.example.plana.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.plana.R;
import com.example.plana.adapter.EventAdapter;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.Todos;
import com.example.plana.database.TodosDB;
import com.example.plana.database.MyDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: PlanA
 * @description:
 */
public class ShowListFragment extends BaseFragment
        implements CompoundButton.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {


    EventAdapter adapter;
    ArrayList<Todos> arr;
    ArrayList<Todos> filtered;
    ListView list;
    SwipeRefreshLayout pullToRefresh;
    SwitchCompat switchDone;

    TextView tvHello;
    Button filterButton;

    RelativeLayout emptyPage;
    MyDatabaseHelper mysql;

    public ShowListFragment() {
    }

    // 获取数据并刷新
    public void refresh(ArrayList<Todos> arr) {
        adapter = new EventAdapter(getActivity(), arr);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        checkEmpty(arr);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mysql = new MyDatabaseHelper(getContext());
        filtered = new ArrayList<>();

        arr = TodosDB.queryAllEvent(mysql);

        list = view.findViewById(R.id.home_list);
        list.setItemsCanFocus(false);
        tvHello = view.findViewById(R.id.tv_hello);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        filterButton = view.findViewById(R.id.filter_button);
        switchDone = view.findViewById(R.id.switch_done);
        emptyPage = view.findViewById(R.id.empty_status);
        switchDone.setChecked(true);

        refresh(arr);

        setHello();

        registerForContextMenu(filterButton);
        filterButton.setOnClickListener(l -> {
            showPopupMenu(filterButton);
        });

        // Pull To Refresh
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(arr);
                SystemClock.sleep(200);
                pullToRefresh.setRefreshing(false);
            }
        });

        // Do not show "Done" Events
        switchDone.setOnCheckedChangeListener(this);

        return view;
    }

    private void setHello() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 5 - 9
        if (5 <= hour && hour <= 9) {
            tvHello.setText("Morning!");
            // 10 - 12
        } else if (10 <= hour && hour <= 12) {
            tvHello.setText("Noon~");
            // 13 - 15
        } else if (13 <= hour && hour <= 16) {
            tvHello.setText("AfterNoon!");
            // 16 - 19
        } else if (17 <= hour && hour <= 19) {
            tvHello.setText("Evening~");
            // 20 - 22
        } else if (20 <= hour && hour <= 22) {
            tvHello.setText("Night!");
            // 23 - 4
        } else {
            tvHello.setText("MidNight~");
        }

        if (!"".equals(account_name)) tvHello.append(" " + account_name + ".");
        else tvHello.append(" Stranger.");
    }


    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_event_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switchDone.setChecked(true);
        switch (item.getItemId()) {
            case R.id.sort_1:
                // Sort by add time
                arr.sort((o1, o2) -> o1.get_id() - o2.get_id());
                refresh(arr);
                break;
            case R.id.sort_2:
                // sort by date
                arr.sort((o1, o2) -> {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                    String date1 = o1.getDate();
                    String date2 = o2.getDate();
                    Date o1Date = null;
                    Date o2Date = null;

                    String time1 = o1.getTime();
                    String time2 = o2.getTime();
                    Date o1Time = null;
                    Date o2Time = null;

                    if (date1.isEmpty() && date2.isEmpty()) return 0;
                    if (date1.isEmpty()) return 1;
                    if (date2.isEmpty()) return -1;

                    try {
                        o1Date = sdf1.parse(date1);
                        o2Date = sdf1.parse(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int resultDate = o1Date.compareTo(o2Date);
                    if (resultDate > 0) return 1;
                    else if (resultDate < 0) return -1;
                    else {
                        // 时间排序
                        if (time1.isEmpty() && time2.isEmpty()) return 0;
                        if (time1.isEmpty()) return 1;
                        if (time2.isEmpty()) return -1;

                        try {
                            o1Time = sdf2.parse(time1);
                            o2Time = sdf2.parse(time2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        int resultTime = o1Time.compareTo(o2Time);
                        if (resultTime > 0) return 1;
                        else if (resultTime < 0) return -1;
                        else return 0;
                    }
                });
                refresh(arr);
                break;
            case R.id.sort_3:
                // Sort by id first
                arr.sort((o1, o2) -> o1.get_id() - o2.get_id());
                // Sort by Level
                arr.sort((o1, o2) -> {
                    double diff = o1.getLevel() - o2.getLevel();
                    if (Double.isNaN(o1.getLevel()) && Double.isNaN(o2.getLevel()))
                        return 0;
                    if (Double.isNaN(o1.getLevel())) return 1;
                    if (Double.isNaN(o2.getLevel())) return -1;
                    return Double.compare(0.0, diff);
                });
                refresh(arr);
                break;
            case R.id.sort_4:
                filtered.clear();
                for (Todos todos : arr) {
                    if (todos.isDone()) {
                        filtered.add(todos);
                    }
                }
                refresh(filtered);
                break;
            case R.id.sort_5:
                switchDone.setChecked(false);
                break;
        }
        return false;
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 0, 1, "Sort by Add-Time");
        menu.add(1, 1, 1, "Sort by Date");
        menu.add(1, 2, 1, "Sort by Priority");
        menu.add(1, 3, 1, "Hide Done Todos");
        menu.add(1, 4, 1, "Hide UnDone Todos");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            refresh(arr);
        } else {
            filtered.clear();
            for (Todos todos : arr) {
                if (!todos.isDone()) {
                    filtered.add(todos);
                }
            }
            refresh(filtered);
        }
    }

    private void checkEmpty(ArrayList<Todos> arr) {
        if (arr.size() < 1) {
            emptyPage.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        } else {
            emptyPage.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }



}
