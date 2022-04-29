package com.example.plana.function.todo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.plana.R;
import com.example.plana.function.MainActivity;
import com.example.plana.base.BaseActivity;
import com.example.plana.config.Constant;
import com.example.plana.database.TodosDB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @program: PlanA
 * @description: 添加 todos
 */
public class AddTodoActivity extends BaseActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AddTodoActivity";

    EditText etContent, etMemo, etDate, etTime, etLevel;
    SwitchCompat switchDate, switchTime, switchLevel;
    Button btnSubmit;

    String finalDate, finalTime;
    float finalLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todos);

        init();

        btnSubmit.setOnClickListener(v -> {
            hideKeyboard(AddTodoActivity.this);
            if (etContent.length() == 0) {
                Toast.makeText(
                        AddTodoActivity.this,
                        "准备做点什么呢？",
                        Toast.LENGTH_SHORT
                ).show();
                etContent.requestFocus();
            } else if (etDate.length() == 0 && etTime.length() > 0) {
                Toast.makeText(
                        AddTodoActivity.this,
                        "哪一天的这个时间点呢？",
                        Toast.LENGTH_SHORT
                ).show();
                switchDate.setChecked(true);
                etDate.performClick();
                etDate.requestFocus();
            } else {
                insertItem();
                directToMainActivity();
            }
        });

    }

    private void directToMainActivity() {
        Intent intent = new Intent(AddTodoActivity.this, MainActivity.class);
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
        }
        startActivity(intent);
        finish();
    }

    private void init() {

        etContent = findViewById(R.id.et_content);
        etMemo = findViewById(R.id.et_memo);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        etLevel = findViewById(R.id.et_level);

        switchDate = findViewById(R.id.switch_date);
        switchTime = findViewById(R.id.switch_time);
        switchLevel = findViewById(R.id.switch_level);

        btnSubmit = findViewById(R.id.btn_submit);

        finalDate = "";
        finalTime = "";
        finalLevel = -1;

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLevel.setOnClickListener(this);

        switchDate.setOnCheckedChangeListener(this);
        switchTime.setOnCheckedChangeListener(this);
        switchLevel.setOnCheckedChangeListener(this);

    }


    public void insertItem() {
        ContentValues todo_values = new ContentValues();
        todo_values.put(TodosDB.content, etContent.getText().toString());
        todo_values.put(TodosDB.memo, etMemo.getText().toString());
        todo_values.put(TodosDB.done, "false");
        todo_values.put(TodosDB.date, finalDate);
        todo_values.put(TodosDB.time, finalTime);
        todo_values.put(TodosDB.level, finalLevel);

        TodosDB.insertEvent(sqlite, todo_values);

        Toast.makeText(
                AddTodoActivity.this,
                "添加成功",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date) {
            setupDate();
//            Log.i(TAG, "选择日期");
        } else if (v.getId() == R.id.et_time) {
            setupTime();
//            Log.i(TAG, "选择时间");
        } else if (v.getId() == R.id.et_level) {
            setupLevel();
//            Log.i(TAG, "选择优先级");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 防止 java 操作 switchCompat 时触发监听器，再次弹出选择窗口
        if (!buttonView.isPressed()) {
            return;
        }
        if (buttonView.getId() == R.id.switch_date) {
            if (isChecked) {
                setupDate();
            } else {
                etDate.setText("");
                finalDate = "";
            }
        } else if (buttonView.getId() == R.id.switch_time) {
            if (isChecked) {
                setupTime();
            } else {
                etTime.setText("");
                finalTime = "";
            }
        } else if (buttonView.getId() == R.id.switch_level) {
            if (isChecked) {
                setupLevel();
            } else {
                etLevel.setText("");
                finalLevel = -1;
            }
        }
    }


    private void setupLevel() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_level, null);
        View titleView = inflater.inflate(R.layout.dialog_add_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("选择优先级");
        RatingBar ratingBar = view.findViewById(R.id.add_rating);

        AlertDialog alertDialog = new AlertDialog
                .Builder(AddTodoActivity.this, R.style.AlertDialogTheme)
                .setCustomTitle(titleView)
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> {
                    if (switchLevel.isChecked() && etLevel.getText().toString().length() == 0) {
                        switchLevel.setChecked(false);
                    }
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    float rating = ratingBar.getRating();
                    etLevel.setText(rating + "");
                    switchLevel.setChecked(true);
                    finalLevel = rating;
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);

    }

    @SuppressLint("SetTextI18n")
    private void setupTime() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_time, null);
        TimePicker timePicker = view.findViewById(R.id.add_time_picker);
        timePicker.setIs24HourView(true);

        AlertDialog alertDialog = new AlertDialog
                .Builder(AddTodoActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> {
                    if (switchTime.isChecked() && etTime.getText().toString().length() == 0) {
                        switchTime.setChecked(false);
                    }
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    int hour = timePicker.getHour();
                    int min = timePicker.getMinute();
                    finalTime = hour + ":" + min;

                    String am_pm = "AM";
                    if (hour > 12) {
                        am_pm = "PM";
                        hour = hour - 12;
                    }
                    etTime.setText(hour + ":" + min + " " + am_pm);
                    switchTime.setChecked(true);
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);

    }

    private void setupDate() {
        Calendar cal = Calendar.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_date, null);
        DatePicker datePicker = view.findViewById(R.id.add_date_picker);

        AlertDialog alertDialog = new AlertDialog
                .Builder(AddTodoActivity.this, R.style.AlertDialogTheme)
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> {
                    if (switchDate.isChecked() && etDate.getText().toString().length() == 0) {
                        switchDate.setChecked(false);
                    }
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    etDate.setText(sdf.format(cal.getTime()));
                    finalDate = sdf.format(cal.getTime());

                    switchDate.setChecked(true);
                })
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);
    }

}
