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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.bean.My;
import com.example.plana.bean.Todos;
import com.example.plana.config.Constant;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TodosDB;
import com.example.plana.database.MyDatabaseHelper;
import com.example.plana.function.todo.EditTodoActivity;
import com.example.plana.utils.TimeCalcUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    // 内部类
    class EventItemHolder {
        TextView tvContent;
        CheckBox cbIsDone;
        TextView tvDate;
    }

    public TodoAdapter(Activity context, ArrayList<Todos> arr) {
        super(context, R.layout.fragment_todo_list_home, arr);
        this.ctx = context;
        this.arrayList = arr;
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
        return position;
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

        // init content & state
        viewHolder.tvContent.setText(arrayList.get(position).getContent());
        viewHolder.cbIsDone.setChecked(arrayList.get(position).isDone());

        // init datetime
        String date = arrayList.get(position).getDate();
        String time = arrayList.get(position).getTime();

        // init remain time
        if (!date.equals("")) {
            long dateParse = 0;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                dateParse = sdf.parse(date).getTime();
                if (!"".equals(time)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);
                    dateParse += simpleDateFormat.parse(time).getTime();
                    dateParse += 8 * 60 * 60 * 1000;        // 转化 time 需要加上 8 hours 【WHY?】
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long nowtime = new Date().getTime();
            if (nowtime > dateParse) {
                String calc = TimeCalcUtil.leftTime(nowtime, dateParse);
                // Today but no exact Time
                if (calc.contains("H") && "".equals(time)) {
                    viewHolder.tvDate.setText("今天");
                } else {
                    viewHolder.tvDate.setTextColor(0xFFDE3143);
                    viewHolder.tvDate.setText(calc);
                }
            } else if (nowtime < dateParse) {
                viewHolder.tvDate.setText(TimeCalcUtil.leftTime(dateParse, nowtime));
            }
        } else {
            viewHolder.tvDate.setText("");
        }

        // CheckBox
        viewHolder.cbIsDone.setOnClickListener(v -> {
            boolean status = viewHolder.cbIsDone.isChecked();
            arrayList.get(position).setDone(status);
            TodosDB.updateEventDoneState(sqlite, arrayList.get(position).get_id() + "", status);
        });

        // init CheckBox Color
        if (arrayList.get(position).getLevel() != -1) {
            viewHolder.cbIsDone.setButtonTintList(ColorStateList((int) (arrayList.get(position).getLevel() * 2)));
        }

        // Click to get detail
        convertView.setOnClickListener(v -> {
            String memo = getItem(position).getMemo();
            if (!"".equals(memo)) {
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
                        todo_values.put(TodosDB._id, item.get_id());
                        todo_values.put(TodosDB.content, item.getContent());
                        todo_values.put(TodosDB.memo, item.getMemo());
                        todo_values.put(TodosDB.done, item.isDone());
                        todo_values.put(TodosDB.date, item.getDate());
                        todo_values.put(TodosDB.time, item.getTime());
                        todo_values.put(TodosDB.level, item.getLevel());
                        DeletedTodosDB.insertTodo(sqlite, todo_values);
                        TodosDB.deleteEventById(sqlite,item.get_id()+"");
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
        // transaction animation
        ctx.overridePendingTransition(R.anim.slide_in, R.anim.fade_out);
//        thisContext.finish();
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