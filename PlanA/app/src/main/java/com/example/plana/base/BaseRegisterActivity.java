package com.example.plana.base;

import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: PlanA
 * @description: BaseRegisterActivity extends BaseActivity
 */
public class BaseRegisterActivity extends BaseActivity {

    // 第一个页面的输入验证
    public boolean checkRegister1(EditText phone, EditText pwd1, EditText pwd2) {
        if (checkEditTextEmpty(phone)) {
            Toast.makeText(this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
            phone.setText("");
            phone.requestFocus();
            return false;
        } else if (checkEditTextEmpty(pwd1)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            pwd1.setText("");
            pwd1.requestFocus();
            return false;
        } else if (checkEditTextEmpty(pwd2)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            pwd2.setText("");
            pwd2.requestFocus();
            return false;
        } else if (!isTextEqual(pwd1, pwd2)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
//            pwd1.setText("");
            pwd2.setText("");
//            pwd1.requestFocus();
            pwd2.requestFocus();
            return false;
        } else if (!checkPhoneValid(phone.getText().toString())) {
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
        if (checkEditTextEmpty(name)) {
            Toast.makeText(this, "请输入您的昵称", Toast.LENGTH_SHORT).show();
            name.setText("");
            name.requestFocus();
            return false;
        } else if (checkEditTextEmpty(birth)) {
            Toast.makeText(this, "请选择您的生日", Toast.LENGTH_SHORT).show();
            birth.setText("");
            birth.requestFocus();
            return false;
        } else if (checkEditTextEmpty(email)) {
            Toast.makeText(this, "请输入您的邮箱", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        } else if (!checkEditTextEmpty(email) && !checkEmailValid(email.getText().toString())) {
            Toast.makeText(this, "请输入合法的邮箱", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        }

        return true;
    }


    /**
     * 手机号正则验证
     */
    private boolean checkPhoneValid(String phoneString) {
        boolean ans = true;
//        String regex = "^1[3-9][0-9]{9}$";
//        ans = phoneString.matches(regex);
        return ans;
    }

    /**
     * 邮箱地址正则表达式
     */
    private boolean checkEmailValid(String emailString) {
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return emailString.matches(regex);
    }

    /**
     * 检验是否为空
     */
    private boolean checkEditTextEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    /**
     * 检验密码是否相等
     */
    private boolean isTextEqual(EditText et1, EditText et2) {
        return et1.getText().toString().equals(et2.getText().toString());
    }

}
