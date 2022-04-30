package com.example.plana.function.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.plana.R;
import com.example.plana.base.MainApplication;
import com.example.plana.bean.MySubject;
import com.example.plana.bean.Plan;
import com.example.plana.bean.TimerRecorder;
import com.example.plana.bean.Todos;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TimerDB;
import com.example.plana.database.TodosDB;
import com.example.plana.function.MainActivity;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.My;
import com.example.plana.function.setting.InfoModifyActivity;
import com.example.plana.function.setting.AboutActivity;
import com.example.plana.function.setting.TrashBinActivity;
import com.example.plana.function.user.LoginActivity;
import com.example.plana.utils.SharedPreferencesUtil;
import com.example.plana.utils.TimeCalcUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: PlanA
 * @description:
 */

public class MyPageFragment extends BaseFragment {

    public static final String TAG = "MyPageFragment";

    TextView myName, myEmail;
    Button btLogOut;

    RelativeLayout rlInfoModify;
    //    RelativeLayout rlNotifyModify;
    RelativeLayout rlUploadToCloud;
    RelativeLayout rlLoadToLocal;
    RelativeLayout rlTrashBin;

    RelativeLayout rlAbout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        myName = view.findViewById(R.id.my_name);
        myEmail = view.findViewById(R.id.my_email);
        btLogOut = view.findViewById(R.id.btn_logout);

        rlInfoModify = view.findViewById(R.id.rl_personal_Info_modify);
//        rlNotifyModify = view.findViewById(R.id.rl_notify_modify);
        rlUploadToCloud = view.findViewById(R.id.rl_upload_to_cloud);
        rlLoadToLocal = view.findViewById(R.id.rl_load_from_cloud);
        rlTrashBin = view.findViewById(R.id.rl_trash_bin);

        rlAbout = view.findViewById(R.id.rl_about);

        myPageSetUp();

        rlInfoModify.setOnClickListener(l -> directToInfoModifyActivity());
//        rlNotifyModify.setOnClickListener(v -> directToNotifyModifyActivity());
        rlUploadToCloud.setOnClickListener(v -> upLoadDataToCloud());
        rlLoadToLocal.setOnClickListener(v -> loadDataToLocal());
        rlTrashBin.setOnClickListener(v -> directToTrashBinActivity());

        rlAbout.setOnClickListener(l -> directToAboutActivity());

        return view;
    }


    private void loadDataToLocal() {

        Toast.makeText(getContext(), "载入中...", Toast.LENGTH_SHORT).show();

        SharedPreferencesUtil backUp = SharedPreferencesUtil.init(
                MainApplication.getAppContext(),
                "BACK_UP"
        );

        Gson gson = new Gson();
        String load = backUp.getString("PLAN_LIST");
        if (StringUtils.isNotEmpty(load)){
            My.plans = gson.fromJson(load, new TypeToken<List<Plan>>() {
            }.getType());
        }
        load = backUp.getString("TODO_LIST");
        if (StringUtils.isNotEmpty(load)){
            My.todosList = gson.fromJson(load, new TypeToken<ArrayList<Todos>>() {
            }.getType());
            TodosDB.dropTable(MainActivity.mainActivity.sqlite);
            for (Todos todo : My.todosList) {
                ContentValues todo_values = new ContentValues();
                todo_values.put(TodosDB.content, todo.getContent());
                todo_values.put(TodosDB.memo, todo.getMemo());
                todo_values.put(TodosDB.done, todo.isDone());
                todo_values.put(TodosDB.date, todo.getDate());
                todo_values.put(TodosDB.time, todo.getTime());
                todo_values.put(TodosDB.level, todo.getLevel());
                TodosDB.insertEvent(MainActivity.mainActivity.sqlite, todo_values);
            }
        }
        load = backUp.getString("SUBJECTS");
        if (StringUtils.isNotEmpty(load)){
            My.mySubjects = gson.fromJson(load, new TypeToken<List<MySubject>>() {
            }.getType());
        }
        load = backUp.getString("RECORDER");
        if (StringUtils.isNotEmpty(load)){
            ArrayList<TimerRecorder> recorders = gson.fromJson(load, new TypeToken<ArrayList<TimerRecorder>>() {
            }.getType());
            TimerDB.dropTable(MainActivity.mainActivity.sqlite);
            for (TimerRecorder recorder : recorders) {
                ContentValues values = new ContentValues();
                values.put(TimerDB.day, recorder.getDay());
                values.put(TimerDB.start_time, recorder.getStartTime());
                values.put(TimerDB.end_time, recorder.getEndTime());
                values.put(TimerDB.time, recorder.getTime());
                values.put(TimerDB.count_type, recorder.getCount_type());
                TimerDB.insertTimerRecorder(MainActivity.mainActivity.sqlite, values);
            }
        }
        load = backUp.getString("DELETE_TODO_LIST");
        if (StringUtils.isNotEmpty(load)){
            ArrayList<Todos> deleteTodos = gson.fromJson(load, new TypeToken<ArrayList<Todos>>() {
            }.getType());
            DeletedTodosDB.dropTable(MainActivity.mainActivity.sqlite);
            for (Todos item : deleteTodos) {
                ContentValues todo_values = new ContentValues();
                todo_values.put(TodosDB._id, item.get_id());
                todo_values.put(TodosDB.content, item.getContent());
                todo_values.put(TodosDB.memo, item.getMemo());
                todo_values.put(TodosDB.done, item.isDone());
                todo_values.put(TodosDB.date, item.getDate());
                todo_values.put(TodosDB.time, item.getTime());
                todo_values.put(TodosDB.level, item.getLevel());
                DeletedTodosDB.insertTodo(MainActivity.mainActivity.sqlite, todo_values);
            }
        }
        new Handler().postDelayed(() -> {
            Toast.makeText(MainApplication.getAppContext(), "载入成功", Toast.LENGTH_SHORT).show();
        }, 3000L);

    }

    private void upLoadDataToCloud() {

        Toast.makeText(getContext(), "上传中...", Toast.LENGTH_SHORT).show();

        Gson gson = new Gson();
        String str_planJSON = gson.toJson(My.plans);
        String str_todoList = gson.toJson(My.todosList);
        String str_subject = gson.toJson(My.mySubjects);
        ArrayList<TimerRecorder> recorders = TimerDB.queryAllTimerRecorder(MainActivity.mainActivity.sqlite);
        String str_Recorders = gson.toJson(recorders);
        ArrayList<Todos> deleteTodos = DeletedTodosDB.queryAllTodos(MainActivity.mainActivity.sqlite);
        String str_DeleteTodos = gson.toJson(deleteTodos);

        backUpStringJson("PLAN_LIST", str_planJSON);
        backUpStringJson("TODO_LIST", str_todoList);
        backUpStringJson("SUBJECTS", str_subject);
        backUpStringJson("RECORDER", str_Recorders);
        backUpStringJson("DELETE_TODO_LIST", str_DeleteTodos);

        new Handler().postDelayed(() -> {
            Toast.makeText(MainApplication.getAppContext(), "上传成功", Toast.LENGTH_SHORT).show();
        }, 3000L);
    }


    private boolean backUpStringJson(String str, String json) {
        if (StringUtils.isEmpty(json)) return false;
        SharedPreferencesUtil back_up = SharedPreferencesUtil.init(
                MainApplication.getAppContext(),
                "BACK_UP"
        ).putString(str, json);
        Log.i("BACK_UP", str + " : " + json);
        return true;
    }


    private void directToNotifyModifyActivity() {
//        Intent intent = new Intent(getContext(), NotifyModifyActivity.class);
//        startActivity(intent);

    }


    private void directToTrashBinActivity() {
        Intent intent = new Intent(getContext(), TrashBinActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到关于界面
     */
    private void directToAboutActivity() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到个人信息修改页面
     */
    public void directToInfoModifyActivity() {
        Intent intent = new Intent(getContext(), InfoModifyActivity.class);
        startActivity(intent);
    }


    /**
     * 初始化
     */
    private void myPageSetUp() {

        // Set User Information
        if (My.Account != null) {
            myName.setText(My.Account.getName());
            if ("".equals(My.Account.getEmail())) {
                myEmail.setText(My.Account.getPhone());
            } else {
                myEmail.setText(My.Account.getEmail());
            }
            btLogOut.setOnClickListener(l -> {
                Toast.makeText(
                        getContext(),
                        "登出",
                        Toast.LENGTH_SHORT
                ).show();
                My.Account = null;
                startActivity(new Intent(getContext(), LoginActivity.class));

                SharedPreferences auto = MainActivity.mainActivity.getSharedPreferences("auto", Context.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.clear();
                autoLogin.apply();

                MainActivity.mainActivity.finish();
                Log.d(TAG, "登出，删除 auto SharedPreferences ");
            });
        } else {
            btLogOut.setText("登录");
            btLogOut.setOnClickListener(l -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                Log.d(TAG, "进入登录页");
            });
        }

    }


}
