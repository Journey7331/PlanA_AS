package com.example.plana.bean;

import android.app.Activity;
import android.content.SharedPreferences;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: PlanA
 * @description:
 */
public class My {

    public static User Account = new User();

    public static ArrayList<Event> todos = new ArrayList<>();
    public static ArrayList<MySubject> mySubjects = new ArrayList<>();


    public static HashMap loadAutoAccount() {
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id",null);
        auto_password = auto.getString("auto_pw",null);
    }



}
