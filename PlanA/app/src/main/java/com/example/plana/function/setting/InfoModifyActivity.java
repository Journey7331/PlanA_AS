package com.example.plana.function.setting;

import static com.example.plana.utils.InputCheckUtil.checkEditTextEmpty;
import static com.example.plana.utils.InputCheckUtil.checkEmailValid;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.bean.My;
import com.example.plana.bean.User;
import com.example.plana.config.Constant;
import com.example.plana.database.UserDB;
import com.example.plana.function.MainActivity;
import com.example.plana.utils.AnimationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */
public class InfoModifyActivity extends BaseActivity {

    TextView tvPhone;
    EditText etNickName;
    EditText etEmail;

    RelativeLayout rlBirth;
    TextView tvBirth;
    EditText etPassword;

    RelativeLayout rlCode;
    TextView tvInfoCode;
    Button btGetCode;
    EditText etCode;

    Button btInfoSubmit;

    Boolean pwdChanged = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_midify);

        initView();

        User user = My.Account;
        tvPhone.setText(user.getPhone());
        etNickName.setText(user.getName());
        etEmail.setText(user.getEmail());
        tvBirth.setText(user.getBirth());

        rlBirth.setOnClickListener(v -> getBirth());

        findViewById(R.id.bt_info_modify_back).setOnClickListener(v -> finish());

        etPassword.setOnFocusChangeListener((view, b) -> {
            if (etCode.getText().length() > 0 && !etCode.getText().toString().equals("")) return;
            if (b) {
                showGetCode(true);
                return;
            } else {
                if (etPassword.getText().length() > 0) {
                    showGetCode(true);
                    return;
                }
            }
            if (!etCode.isFocused()) showGetCode(false);
        });

        btGetCode.setOnClickListener(view -> {
            view.setEnabled(false);
            Toast.makeText(this, "已发送验证码", Toast.LENGTH_SHORT).show();

            // TODO sendVerifyCode
//            sendVerifyCode();
            timer.start();
        });

        btInfoSubmit.setOnClickListener(l -> {
            if (!checkInput()) return;
            String pwd = user.getPwd();
            if (pwdChanged) pwd = etPassword.getText().toString();
            User input = new User(
                    user.getId(),
                    user.getPhone(),
                    pwd,
                    etNickName.getText().toString(),
                    etEmail.getText().toString(),
                    tvBirth.getText().toString()
            );
            if (input.equals(user)) {
                finish();
                Toast.makeText(this, "无更改", Toast.LENGTH_SHORT).show();
            } else {
                My.Account = input;
                UserDB.updateUser(sqlite, user.getId(), getUserContentValues(input));
                updateToMainActivity();
                Toast.makeText(this, "更改成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateToMainActivity() {
        Intent intent = new Intent(InfoModifyActivity.this, MainActivity.class);
        if (MainActivity.mainActivity != null) MainActivity.mainActivity.finish();
        My.page = R.id.page_5;
        startActivity(intent);
        finish();
    }

    private ContentValues getUserContentValues(User input) {
        ContentValues values = new ContentValues();
        values.put(UserDB.phone, input.getPhone());
        values.put(UserDB.pwd, input.getPwd());
        values.put(UserDB.name, input.getName());
        values.put(UserDB.birth, input.getBirth());
        values.put(UserDB.email, input.getEmail());
        return values;
    }

    private boolean checkInput() {
        if (checkEditTextEmpty(etNickName)) {
            Toast.makeText(this, "请输入您的昵称", Toast.LENGTH_SHORT).show();
            etNickName.setText("");
            etNickName.requestFocus();
            return false;
        } else if (checkEditTextEmpty(etEmail)) {
            Toast.makeText(this, "请输入您的邮箱", Toast.LENGTH_SHORT).show();
            etEmail.setText("");
            etEmail.requestFocus();
            return false;
        } else if (!checkEditTextEmpty(etEmail) && !checkEmailValid(etEmail.getText().toString())) {
            Toast.makeText(this, "请输入合法的邮箱", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        } else if (!checkEditTextEmpty(etPassword)) {
            if (checkEditTextEmpty(etCode)) {
                Toast.makeText(this, "请获取验证码", Toast.LENGTH_SHORT).show();
                return false;
            } else if (etCode.getText().toString().length() != 4) {
                Toast.makeText(this, "验证码有误", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                pwdChanged = true;
                return true;
            }
        }
        return true;
    }

    private void getBirth() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_date, null);
        final DatePicker datePicker = view.findViewById(R.id.add_date_picker);
        final Calendar cal = Calendar.getInstance();

        AlertDialog alertDialog = new AlertDialog
                .Builder(this, R.style.AlertDialogTheme)
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", (dialog, which) -> {
                    cal.set(Calendar.YEAR, datePicker.getYear());
                    cal.set(Calendar.MONTH, datePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    tvBirth.setText(sdf.format(cal.getTime()));
                }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Constant.MyColor.BlueGrey);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Constant.MyColor.BlueGrey);
    }


    CountDownTimer timer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            btGetCode.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btGetCode.setEnabled(true);
            btGetCode.setText("获取验证码");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void initView() {

        tvPhone = findViewById(R.id.tv_info_phone_show);
        etNickName = findViewById(R.id.et_info_nickname);
        etEmail = findViewById(R.id.et_info_email);

        rlBirth = findViewById(R.id.rl_info_birth_part);
        tvBirth = findViewById(R.id.tv_info_birth_show);
        etPassword = findViewById(R.id.et_info_password);

        rlCode = findViewById(R.id.rl_info_code_part);
        tvInfoCode = findViewById(R.id.tv_info_code);
        etCode = findViewById(R.id.et_info_code);
        btGetCode = findViewById(R.id.bt_get_code);

        btInfoSubmit = findViewById(R.id.btn_info_submit);

    }


    /**
     * 验证码的显示和隐藏
     */
    private void showGetCode(boolean b) {
        long dur = 100;
        if (b) {
            AnimationUtils.showAndHiddenAnimation(tvInfoCode, AnimationUtils.AnimationState.STATE_SHOW, dur);
            AnimationUtils.showAndHiddenAnimation(rlCode, AnimationUtils.AnimationState.STATE_SHOW, dur);
        } else {
            AnimationUtils.showAndHiddenAnimation(tvInfoCode, AnimationUtils.AnimationState.STATE_HIDDEN, dur);
            AnimationUtils.showAndHiddenAnimation(rlCode, AnimationUtils.AnimationState.STATE_HIDDEN, dur);
        }
    }
}
