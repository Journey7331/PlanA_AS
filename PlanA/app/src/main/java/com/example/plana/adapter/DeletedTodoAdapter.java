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
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.database.TimerDB;
import com.example.plana.database.TodosDB;
import com.example.plana.function.todo.EditTodoActivity;
import com.example.plana.utils.TimeCalcUtil;
import com.zhuangfei.timetable.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */
public class DeletedTodoAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    ArrayList<Todos> arrayList;
    Activity ctx;
    MyDatabaseHelper sqlite;

    RelativeLayout rlEmpty;

    // 内部类
    static class DeletedTodoItemHolder {
        TextView tvContent;
        CheckBox cbIsDone;
    }

    public DeletedTodoAdapter(Activity context, ArrayList<Todos> arr, RelativeLayout rlEmpty) {
        super(context, R.layout.activity_trash_bin, arr);
        this.ctx = context;
        this.arrayList = arr;
        this.sqlite = new MyDatabaseHelper(context);
        this.rlEmpty = rlEmpty;
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
        return position;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DeletedTodoItemHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_deleted_todos_list, parent, false);
            viewHolder = new DeletedTodoItemHolder();
            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.cbIsDone = convertView.findViewById(R.id.cb_is_done);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeletedTodoItemHolder) convertView.getTag();
        }

        viewHolder.tvContent.setText(arrayList.get(position).getContent());
        viewHolder.cbIsDone.setChecked(arrayList.get(position).isDone());
        viewHolder.cbIsDone.setClickable(false);

        if (arrayList.get(position).getLevel() != -1) {
            viewHolder.cbIsDone.setButtonTintList(ColorStateList((int) (arrayList.get(position).getLevel() * 2)));
        }

        convertView.setOnClickListener(v -> {
            View titleView = inflater.inflate(R.layout.dialog_add_title, null);
            TextView title = titleView.findViewById(R.id.dialog_title);
            title.setText("删除 / 还原");

            AlertDialog alertDialog = new AlertDialog
                    .Builder(getContext(), R.style.AlertDialogTheme)
                    .setCustomTitle(titleView)
                    .setPositiveButton("还原", (dialog, which) -> {
                        Todos item = getItem(position);
                        ContentValues todo_values = new ContentValues();
                        todo_values.put(TodosDB._id, item.get_id());
                        todo_values.put(TodosDB.content, item.getContent());
                        todo_values.put(TodosDB.memo, item.getMemo());
                        todo_values.put(TodosDB.done, item.isDone());
                        todo_values.put(TodosDB.date, item.getDate());
                        todo_values.put(TodosDB.time, item.getTime());
                        todo_values.put(TodosDB.level, item.getLevel());

                        TodosDB.insertEvent(sqlite, todo_values);
                        DeletedTodosDB.deleteTodoById(sqlite, item.get_id() + "");
                        My.todosList.add(item);

                        remove(item);
                        notifyDataSetChanged();

                        checkEmpty();
                        Toast.makeText(getContext(), "已还原", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("删除", (dialog, which) -> {

                        DeletedTodosDB.deleteTodoById(sqlite, (getItem(position)).get_id() + "");
                        remove(getItem(position));
                        notifyDataSetChanged();

                        checkEmpty();
                        Toast.makeText(getContext(), "成功删除", Toast.LENGTH_SHORT).show();
                    })
                    .setNeutralButton("取消", null).create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);
            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Constant.MyColor.BlueGrey);
        });

        return convertView;
    }


    /**
     * 显示列表为空图
     */
    public void checkEmpty() {
        if (arrayList.size() > 0) {
            rlEmpty.setVisibility(View.INVISIBLE);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 全部还原
     */
    public void recoverAll() {
        if (arrayList.size() == 0) {
            Toast.makeText(getContext(), "无内容可以恢复", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Todos> list = new ArrayList<>(arrayList);
        for (Todos item : list) {
            ContentValues todo_values = new ContentValues();
            todo_values.put(TodosDB._id, item.get_id());
            todo_values.put(TodosDB.content, item.getContent());
            todo_values.put(TodosDB.memo, item.getMemo());
            todo_values.put(TodosDB.done, item.isDone());
            todo_values.put(TodosDB.date, item.getDate());
            todo_values.put(TodosDB.time, item.getTime());
            todo_values.put(TodosDB.level, item.getLevel());

            TodosDB.insertEvent(sqlite, todo_values);
            DeletedTodosDB.deleteTodoById(sqlite, item.get_id() + "");
            My.todosList.add(item);
            remove(item);
        }

        notifyDataSetChanged();
        rlEmpty.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "已还原全部", Toast.LENGTH_SHORT).show();
    }

    /**
     * 全部删除
     */
    public void deleteAll() {
        if (arrayList.size() == 0) {
            Toast.makeText(getContext(), "无内容可以删除", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Todos> list = new ArrayList<>(arrayList);
        for (Todos item : list) {
            remove(item);
            DeletedTodosDB.deleteTodoById(sqlite, item.get_id() + "");
        }
        notifyDataSetChanged();
        rlEmpty.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "成功删除全部", Toast.LENGTH_SHORT).show();
    }


    // Override CheckBox TintColor
    private ColorStateList ColorStateList(int level) {
        // 【 FF -> transparency 】
        int[] levelColors = new int[]{
                0xFFB7EFC5, 0xFF92E6A7, 0xFF6EDE8A, 0xFF4AD66D, 0xFF2DC653,
                0xFF25A244, 0xFF208B3A, 0xFF1A7431, 0xFF155D27, 0xFF10451D,
        };
        int color = levelColors[level - 1];
        int[] colors = new int[]{color, color, color, color, color, color};
        int[][] states = new int[6][];

        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }


}