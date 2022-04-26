package com.example.plana.function.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.plana.R;
import com.example.plana.adapter.TodoAdapter;
import com.example.plana.config.MyConfig;
import com.example.plana.config.MyConfigConstant;
import com.example.plana.function.MainActivity;
import com.example.plana.function.todo.AddTodoActivity;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.database.TodosDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.phoenix.PullToRefreshView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

/**
 * @program: PlanA
 * @description:
 */
public class ShowListFragment extends BaseFragment
        implements PopupMenu.OnMenuItemClickListener {

    TodoAdapter adapter;
    ArrayList<Todos> arr;
    ArrayList<Todos> filtered;

    TextView tvHello;
    ListView listView;
    PullToRefreshView pullToRefresh;

    // true： hide done things
    // false：show done things
    SwitchCompat switchHideDone;

    ImageButton filterButton;
    FloatingActionButton fab;

    RelativeLayout emptyPage;

    Map<String, String> todoConfig;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list_home, container, false);
        listView = view.findViewById(R.id.home_list);
        listView.setItemsCanFocus(false);
        tvHello = view.findViewById(R.id.tv_hello);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        filterButton = view.findViewById(R.id.filter_button);
        switchHideDone = view.findViewById(R.id.switch_done);
        fab = view.findViewById(R.id.add_todo_fab);
        emptyPage = view.findViewById(R.id.empty_status);

        sayHello();

        filtered = new ArrayList<>();
        My.todosList = TodosDB.queryAllEvent(MainActivity.mainActivity.sqlite);
        arr = My.todosList;
        refreshListView(arr);

        registerForContextMenu(filterButton);


        // setOnClickListener
        filterButton.setOnClickListener(l -> showPopupMenu(filterButton));

        pullToRefresh.setOnRefreshListener(() -> pullToRefresh.postDelayed(() -> {
            refreshListView(arr);
            pullToRefresh.setRefreshing(false);
        }, 1000));

        fab.setOnClickListener(v -> directToAddActivity());

        switchHideDone.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                filtered.clear();
                for (Todos todos : arr) {
                    if (!todos.isDone()) {
                        filtered.add(todos);
                    }
                }
                todoConfig.put(MyConfigConstant.Todo.CONFIG_HIDE_DONE_TODO, MyConfigConstant.VALUE_TRUE);
                refreshListView(filtered);
            } else {
                todoConfig.put(MyConfigConstant.Todo.CONFIG_HIDE_DONE_TODO, MyConfigConstant.VALUE_FALSE);
                refreshListView(arr);
            }
            MyConfig.saveTodoConfig(todoConfig);
        });

        loadTodoConfig();

        return view;
    }

    private void loadTodoConfig() {
        todoConfig = MyConfig.loadTodoConfig();
        String value = todoConfig.get(MyConfigConstant.Todo.CONFIG_HIDE_DONE_TODO);
        switch (value) {
            case MyConfigConstant.VALUE_TRUE:
                switchHideDone.setChecked(true);
                break;
            case MyConfigConstant.VALUE_FALSE:
                switchHideDone.setChecked(false);
                break;
        }

        value = todoConfig.get(MyConfigConstant.Todo.CONFIG_TODO_SORT);
        switch (value) {
            case MyConfigConstant.Todo.SORT_BY_ADD_TIME:
                sortByAddTime();
                break;
            case MyConfigConstant.Todo.SORT_BY_DATE:
                sortByDate();
                break;
            case MyConfigConstant.Todo.SORT_BY_PRIORITY:
                sortByLevel();
                break;
        }
    }


    /**
     * 转到 AddTodoActivity
     */
    private void directToAddActivity() {
        Intent intent = new Intent(getContext(), AddTodoActivity.class);
        startActivity(intent);
    }


    /**
     * 获取数据并刷新
     */
    public void refreshListView(ArrayList<Todos> arr) {
        adapter = new TodoAdapter(getActivity(), arr);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        checkViewEmpty(arr);
    }


    /**
     * 左上角的打招呼
     */
    private void sayHello() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String helloWords;
        if (5 <= hour && hour <= 9) helloWords = "早上好！";
        else if (10 <= hour && hour <= 12) helloWords = "中午好~";
        else if (13 <= hour && hour <= 16) helloWords = "下午好！";
        else if (17 <= hour && hour <= 19) helloWords = "傍晚好~";
        else if (20 <= hour && hour <= 22) helloWords = "晚上好！";
        else helloWords = "夜深啦！";
        tvHello.append(helloWords);

        if (My.Account == null) tvHello.append(" Stranger.");
        else tvHello.append(" " + My.Account.getName() + ".");
    }


    /**
     * 下拉选项弹窗
     */
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.sort_event_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_1:
                sortByAddTime();
                todoConfig.put(MyConfigConstant.Todo.CONFIG_TODO_SORT, MyConfigConstant.Todo.SORT_BY_ADD_TIME);
                MyConfig.saveTodoConfig(todoConfig);
                break;
            case R.id.sort_2:
                sortByDate();
                todoConfig.put(MyConfigConstant.Todo.CONFIG_TODO_SORT, MyConfigConstant.Todo.SORT_BY_DATE);
                MyConfig.saveTodoConfig(todoConfig);
                break;
            case R.id.sort_3:
                sortByLevel();
                todoConfig.put(MyConfigConstant.Todo.CONFIG_TODO_SORT, MyConfigConstant.Todo.SORT_BY_PRIORITY);
                MyConfig.saveTodoConfig(todoConfig);
                break;
            case R.id.sort_4:
                // hide done
                switchHideDone.setChecked(true);
                break;
        }
        return false;
    }

    private void sortByLevel() {
        // Sort by id first
        arr.sort(Comparator.comparingInt(Todos::get_id));
        // Sort by Level
        arr.sort((o1, o2) -> {
            double diff = o1.getLevel() - o2.getLevel();
            if (Double.isNaN(o1.getLevel()) && Double.isNaN(o2.getLevel()))
                return 0;
            if (Double.isNaN(o1.getLevel())) return 1;
            if (Double.isNaN(o2.getLevel())) return -1;
            return Double.compare(0.0, diff);
        });
        refreshListView(arr);
    }

    private void sortByDate() {
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
                return Integer.compare(resultTime, 0);
            }
        });
        refreshListView(arr);
    }

    private void sortByAddTime() {
        arr.sort(Comparator.comparingInt(Todos::get_id));
        refreshListView(arr);
    }

    /**
     * 检查list是否为空，刷新不同的界面
     */
    private void checkViewEmpty(ArrayList<Todos> arr) {
        if (arr.size() < 1) {
            emptyPage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            emptyPage.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }

}
