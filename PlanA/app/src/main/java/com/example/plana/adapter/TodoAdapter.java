package com.example.plana.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.adapter.listener.OnScrollToListener;
import com.example.plana.adapter.listener.OnTodoCheckedChangeListener;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TodosDB;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.function.todo.EditTodoActivity;
import com.example.plana.utils.ColorUtil;
import com.example.plana.utils.TimeCalcUtil;
import com.yalantis.phoenix.PullToRefreshView;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */
public class TodoAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    ArrayList<Todos> arrayList;
    Activity ctx;
    MyDatabaseHelper sqlite;
    OnTodoCheckedChangeListener checkedChangeListener;

    RelativeLayout emptyPage;
    PullToRefreshView pullToRefresh;

    public void setOnTodoCheckedChangeListener(OnTodoCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    // 内部类
    class EventItemHolder {
        TextView tvContent;
        CheckBox cbIsDone;
        TextView tvDate;
    }

    public TodoAdapter(Activity context, ArrayList<Todos> arr, RelativeLayout emptyPage, PullToRefreshView pullToRefresh) {
        super(context, R.layout.fragment_todo_list_home, arr);
        this.ctx = context;
        this.arrayList = arr;
        this.emptyPage = emptyPage;
        this.pullToRefresh = pullToRefresh;
        this.sqlite = new MyDatabaseHelper(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Nullable
    @Override
    public Todos getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getId();
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final EventItemHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_todos_list, parent, false);

            viewHolder = new EventItemHolder();
            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.cbIsDone = convertView.findViewById(R.id.cb_is_done);
            viewHolder.tvDate = convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EventItemHolder) convertView.getTag();
        }

        Todos todos = arrayList.get(position);

        // init content & state
        viewHolder.tvContent.setText(todos.getContent());
        viewHolder.cbIsDone.setChecked(todos.isDone());

        // init datetime
        String dateTime = TimeCalcUtil.getTodoDateStr(todos.getDate(), todos.getTime());
        if (dateTime.contains("-")) {
            viewHolder.tvDate.setTextColor(0xFFDE3143);
            dateTime = dateTime.substring(1);
        } else {
            viewHolder.tvDate.setTextColor(ctx.getResources().getColor(R.color.dark_grey));
        }
        viewHolder.tvDate.setText(dateTime);

        // CheckBox
        viewHolder.cbIsDone.setOnClickListener(v -> {
            boolean status = viewHolder.cbIsDone.isChecked();
            todos.setDone(status);
            checkedChangeListener.freshTodoListOnCheckedChange(position, status);
            TodosDB.updateEventDoneState(sqlite, todos.getId() + "", status);
        });

        // init CheckBox Color
        if (todos.getLevel() != -1) {
            viewHolder.cbIsDone.setButtonTintList(ColorUtil.ColorStateList((int) (todos.getLevel() * 2)));
        } else {
            viewHolder.cbIsDone.setButtonTintList(ColorUtil.ColorDefaultList(ctx.getResources().getColor(R.color.grey)));
        }

        // Click to get detail
        convertView.setOnClickListener(v -> {
            String memo = getItem(position).getMemo();
            if (StringUtils.isNotEmpty(memo)) {
                Toast.makeText(getContext(), memo, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "没有备注", Toast.LENGTH_SHORT).show();
            }
        });

        // LongPress
        convertView.setOnLongClickListener(v -> {
            View titleView = inflater.inflate(R.layout.dialog_add_title, null);
            TextView title = titleView.findViewById(R.id.dialog_title);
            title.setText("删除 / 更改");

            AlertDialog alertDialog = new AlertDialog
                    .Builder(getContext(), R.style.AlertDialogTheme)
                    .setCustomTitle(titleView)
                    .setPositiveButton("更改", (dialog, which) -> {
                        My.editTodo = getItem(position);
                        directToEditActivity();
                    })
                    .setNegativeButton("删除", (dialog, which) -> {
                        Todos item = getItem(position);
                        ContentValues todo_values = new ContentValues();
                        todo_values.put(TodosDB._id, item.getId());
                        todo_values.put(TodosDB.content, item.getContent());
                        todo_values.put(TodosDB.memo, item.getMemo());
                        todo_values.put(TodosDB.done, item.isDone());
                        todo_values.put(TodosDB.date, item.getDate());
                        todo_values.put(TodosDB.time, item.getTime());
                        todo_values.put(TodosDB.level, item.getLevel());
                        DeletedTodosDB.insertTodo(sqlite, todo_values);
                        TodosDB.deleteEventById(sqlite, item.getId() + "");
                        My.todosList.remove(item);
                        remove(item);
                        notifyDataSetChanged();

                        if (arrayList.size() < 1) {
                            ctx.findViewById(R.id.home_list).setVisibility(View.INVISIBLE);
                            ctx.findViewById(R.id.empty_status).setVisibility(View.VISIBLE);
                        }

                        Toast.makeText(getContext(), "成功删除", Toast.LENGTH_SHORT).show();
                    })
                    .setNeutralButton("取消", null).create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Constant.MyColor.BlueGrey);

            return true;
        });

        return convertView;
    }

    private void directToEditActivity() {
        Intent intent = new Intent(ctx, EditTodoActivity.class);
        ctx.startActivity(intent);
    }

    public void sortDoneToBottom(int position, boolean check, boolean hide) {
        Todos remove = arrayList.remove(position);
        if (check) {
            if (!hide) {
                int i = 0;
                for (; i < getCount(); i++) {
                    if (arrayList.get(i).isDone()) break;
                }
                arrayList.add(i, remove);
            }
        } else {
            int i = getCount() - 1;
            for (; i >= 0; i--) {
                if (!arrayList.get(i).isDone()) break;
            }
            arrayList.add(i + 1, remove);
        }
        notifyDataSetChanged();
    }

    /**
     * 检查list是否为空，刷新不同的界面
     */
    public void checkViewEmpty() {
        if (arrayList.size() < 1) {
            emptyPage.setVisibility(View.VISIBLE);
            pullToRefresh.setVisibility(View.INVISIBLE);
        } else {
            emptyPage.setVisibility(View.INVISIBLE);
            pullToRefresh.setVisibility(View.VISIBLE);
        }
    }

}