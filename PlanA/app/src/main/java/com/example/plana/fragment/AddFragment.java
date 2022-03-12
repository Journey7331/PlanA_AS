package com.example.plana.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.widget.SwitchCompat;

import com.example.plana.R;
import com.example.plana.base.BaseFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * @program: PlanA
 * @description: AddFragment extends BaseFragment
 */
public class AddFragment extends BaseFragment
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText etContent, etMemo, etDate, etTime, etLevel;
    SwitchCompat switchDate, switchTime, switchLevel;
    Button btnSubmit;

    String finalDate, finalTime;
    float finalLevel;

    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        init(view);

        btnSubmit.setOnClickListener(v -> {
            hideKeyboard(getActivity());
            if (etContent.length() == 0) {
                Toast.makeText(getContext(), "Tell Me What You Wanna Do.", Toast.LENGTH_SHORT).show();
                etContent.requestFocus();
            } else if (etDate.length() == 0 && etTime.length() > 0) {
                Toast.makeText(getContext(), "Choose A Day.", Toast.LENGTH_SHORT).show();
                switchDate.setChecked(true);
                etDate.performClick();
                etDate.requestFocus();
            } else {
                insertItem();
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.page_1);
            }
        });
        return view;
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
        ContentValues values = new ContentValues();
        values.put(EventDB.content, etContent.getText().toString());
        values.put(EventDB.memo, etMemo.getText().toString());
        values.put(EventDB.done, "false");
        values.put(EventDB.date, finalDate);
        values.put(EventDB.time, finalTime);
        values.put(EventDB.level, finalLevel);
        // TODO Put Location

        MyDatabaseHelper mysql = new MyDatabaseHelper(getContext());
        EventDB.insertEvent(mysql, values);
//        Toast.makeText(getContext(), "Successfully Added!  " + "Content: " + etContent.getText().toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Successfully Added!", Toast.LENGTH_SHORT).show();
    }

    // Press EditView
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date) {
            setupDate();
            Log.i("addEvent", "** Select Date.");
        } else if (v.getId() == R.id.et_time) {
            setupTime();
            Log.i("addEvent", "** Select Time.");
        } else if (v.getId() == R.id.et_level) {
            setupLevel();
            Log.i("addEvent", "** Select Level.");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_level, null);
        View titleView = inflater.inflate(R.layout.add_title, null);
        TextView title = titleView.findViewById(R.id.dialog_title);
        title.setText("Choose Priority");
        RatingBar ratingBar = view.findViewById(R.id.add_rating);
        builder.setCustomTitle(titleView);
        builder.setView(view);

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if (switchLevel.isChecked() && etLevel.getText().toString().length() == 0) {
                switchLevel.setChecked(false);
            }
        });
        builder.setPositiveButton("Submit", (dialog, which) -> {
            float rating = ratingBar.getRating();
            etLevel.setText(rating + "");
            switchLevel.setChecked(true);
            finalLevel = rating;
        });
        builder.create().show();
    }

    @SuppressLint("SetTextI18n")
    private void setupTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_time, null);
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
        View view = inflater.inflate(R.layout.add_date, null);
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