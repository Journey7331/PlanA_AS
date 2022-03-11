package com.example.plana.base;

import android.widget.EditText;
import android.widget.Toast;

/**
 * @program: PlanA
 * @description: BaseRegisterActivity extends BaseActivity
 */
public abstract class BaseRegisterActivity extends BaseActivity {

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
        }


        // TODO: checkPhoneExist(phone.getText().toString())
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
            Toast.makeText(this, "请输入您的邮箱，以便于找回密码", Toast.LENGTH_SHORT).show();
            email.setText("");
            email.requestFocus();
            return false;
        }

        // TODO: checkEmailValid(email.getText().toString())

        return true;
    }

    // 检验是否为空
    private boolean checkEditTextEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    // 检验密码是否相等
    private boolean isTextEqual(EditText et1, EditText et2) {
        return et1.getText().toString().equals(et2.getText().toString());
    }

}
