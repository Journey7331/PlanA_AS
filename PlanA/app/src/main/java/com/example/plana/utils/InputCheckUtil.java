package com.example.plana.utils;

import android.widget.EditText;

/**
 * @program: PlanA
 * @description:
 */
public class InputCheckUtil {


    /**
     * 邮箱地址正则表达式
     */
    public static boolean checkEmailValid(String emailString) {
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return emailString.matches(regex);
    }

    /**
     * 检验是否为空
     */
    public static boolean checkEditTextEmpty(EditText editText) {
        return editText.getText().toString().length() == 0;
    }

    /**
     * 检验密码是否相等
     */
    public static boolean isTextEqual(EditText et1, EditText et2) {
        return et1.getText().toString().equals(et2.getText().toString());
    }

    /**
     * 手机号正则验证
     */
    public static boolean checkPhoneValid(String phoneString) {
        boolean ans = true;
//        String regex = "^1[3-9][0-9]{9}$";
//        ans = phoneString.matches(regex);
        return ans;
    }







}
