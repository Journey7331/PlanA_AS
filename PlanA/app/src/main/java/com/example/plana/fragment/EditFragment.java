package com.example.plana.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.plana.R;
import com.example.plana.base.BaseFragment;
import com.example.plana.database.EventDB;
import com.example.plana.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */

public class EditFragment extends BaseFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText etContent, etMemo, etDate, etTime, etLevel;
    SwitchCompat switchDate, switchTime, switchLevel;
    Button tvBack, btnSubmit;

    String finalDate, finalTime;
    float finalLevel;

    Event event;

    public EditFragment(Event event) {
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, container, false);

        init(v);
        setUpEvent();

        // 聚焦content
        etContent.requestFocus();
        return v;
    }

    private void init(View view) {
        etContent = view.findViewById(R.id.et_content);
        etMemo = view.findViewById(R.id.et_memo);
        etDate = view.findViewById(R.id.et_date);
        etTime = view.findViewById(R.id.et_time);
        etLevel = view.findViewById(R.id.et_level);

        switchDate = view.findViewById(R.id.switch_date);
        switchTime = view.findViewById(R.id.switch_time);
        switchLevel = view.findViewById(R.id.switch_level);

        btnSubmit = view.findViewById(R.id.btn_submit);
        tvBack = view.findViewById(R.id.edit_back);

        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLevel.setOnClickListener(this);

        switchDate.setOnCheckedChangeListener(this);
        switchTime.setOnCheckedChangeListener(this);
        switchLevel.setOnCheckedChangeListener(this);

        tvBack.setOnClickListener(l -> getActivity().getSupportFragmentManager().popBackStack());
        btnSubmit.setOnClickListener(l -> {
            if (etContent.length() == 0) {
                Toast.makeText(getContext(), "Please Add Something.", Toast.LENGTH_SHORT).show();
                etContent.requestFocus();
            } else if (etDate.length() == 0 && etTime.length() > 0) {
                Toast.makeText(getContext(), "Please Set A DDL.", Toast.LENGTH_SHORT).show();
                // 因为避免了【初始化的时候触发监听器】，这里修改并不会调用 onCheckedChanged
                switchDate.setChecked(true);
                // 模拟点击 View
                etDate.performClick();
                etDate.requestFocus();
            } else {
                editItem();
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.page_1);
            }
        });
    }

    private void setUpEvent() {
        etContent.setText(event.getContent());
        etMemo.setText(event.getMemo());
        finalDate = event.getDate();
        finalTime = event.getTime();
        finalLevel = event.getLevel();

        if (finalDate.length() > 0) {
            etDate.setText(finalDate);
            switchDate.setChecked(true);
        }
        if (finalTime.length() > 0) {
            etTime.setText(finalTime);
            switchTime.setChecked(true);
        }
        if (finalLevel > 0) {
            etLevel.setText(finalLevel + "");
            switchLevel.setChecked(true);
        }
    }

    private void editItem() {
        ContentValues values = new ContentValues();
        values.put(EventDB.content, etContent.getText().toString());
        values.put(EventDB.memo, etMemo.getText().toString());
        values.put(EventDB.date, finalDate);
        values.put(EventDB.time, finalTime);
        values.put(EventDB.level, finalLevel);
        //TODO Put Location

        MyDatabaseHelper mysql = new MyDatabaseHelper(getContext());
        EventDB.updateEventById(mysql, event.get_id() + "", values);
        Toast.makeText(getContext(), "Successfully Edited!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date) {
            setupDate();
        } else if (v.getId() == R.id.et_time) {
            setupTime();
        } else if (v.getId() == R.id.et_level) {
            setupLevel();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 防止初始化的时候触发监听器
        if (!buttonView.isPressed()) return;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_level, null);
        View titleView = inflater.inflate(R.layout.dialog_add_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("Choose Priority");
        RatingBar ratingBar = view.findViewById(R.id.add_rating);
        builder.setCustomTitle(titleView);
        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchLevel.isChecked() && etLevel.getText().toString().length() == 0) {
                    switchLevel.setChecked(false);
                }
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float rating = ratingBar.getRating();
                etLevel.setText(rating + "");
                switchLevel.setChecked(true);
                finalLevel = rating;
            }
        });

        builder.create().show();
    }

    private void setupTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_time, null);
        TimePicker timePicker = view.findViewById(R.id.add_time_picker);
        timePicker.setIs24HourView(true);
        builder.setView(view);

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if (switchTime.isChecked() && etTime.getText().toString().length() == 0) {
                switchTime.setChecked(false);
            }
        });
        builder.setPositiveButton("Submit", (dialog, which) -> {
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
        });

        builder.create().show();
    }

    private void setupDate() {
        Calendar cal = Calendar.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_date, null);
        DatePicker datePicker = view.findViewById(R.id.add_date_picker);
        builder.setView(view);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (switchDate.isChecked() && etDate.getText().toString().length() == 0) {
                    switchDate.setChecked(false);
                }
            }
        });

        builder.setPositiveButton("Submit", (dialog, which) -> {
            cal.set(Calendar.YEAR, datePicker.getYear());
            cal.set(Calendar.MONTH, datePicker.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            etDate.setText(sdf.format(cal.getTime()));
            finalDate = sdf.format(cal.getTime());

            switchDate.setChecked(true);
        });

        builder.create().show();
    }

}
