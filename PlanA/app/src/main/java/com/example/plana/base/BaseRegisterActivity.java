package com.example.plana.base;

import android.widget.EditText;
import android.widget.Toast;

import com.example.plana.utils.InputCheckUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: PlanA
 * @description: BaseRegisterActivity extends BaseActivity
 */
public class BaseRegisterActivity extends BaseActivity {

    // 第一个页面的输入验证
    public boolean checkRegister1(EditText phone, EditText pwd1, EditText pwd2) {
        if (InputCheckUtil.checkEditTextEmpty(phone)) {
            Toast.makeText(this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
            phone.setText("");
            phone.requestFocus();
            return false;
        } else if (InputCheckUtil.checkEditTextEmpty(pwd1)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            pwd1.setText("");
            pwd1.requestFocus();
            return false;
        } else if (InputCheckUtil.checkEditTextEmpty(pwd2)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            pwd2.setText("");
            pwd2.requestFocus();
            return false;
        } else if (!InputCheckUtil.isTextEqual(pwd1, pwd2)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
//            pwd1.setText("");
            pwd2.setText("");
//            pwd1.requestFocus();
            pwd2.requestFocus();
            return false;
        } else if (!InputCheckUtil.checkPhoneValid(phone.getText().toString())) {
            Toast.makeText(this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
            phone.setText("");
            phone.requestFocus();
            return false;
        }

        // TODO: get phone code (need api)

        return true;
    }


    // 第二个页面的输入验证
    public boolean checkRegister2(EditText name, EditText birth, EditText email) {
        if (InputCheckUtil.checkEditTextEmpty(name)) {
            Toast.makeText(this, "请输入您的昵称", Toast.LENGTH_SHORT).show();
            name.setText("");
            name.requestFocus();
            return false;
        } else if (InputCheckUtil.checkEditTextEmpty(birth)) {
            Toast.makeText(this, "请选择您的生日", Toast.LENGTH_SHORT).show();
            birth.setText("");
            birth.requestFocus();
            return false;
        } else if (InputCheckUtil.checkEditTextEmpty(email)) {
            Toast.makeText(this, "请输入您的邮箱", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        } else if (!InputCheckUtil.checkEditTextEmpty(email)
                && !InputCheckUtil.checkEmailValid(email.getText().toString())) {
            Toast.makeText(this, "请输入合法的邮箱", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        }

        return true;
    }




}
