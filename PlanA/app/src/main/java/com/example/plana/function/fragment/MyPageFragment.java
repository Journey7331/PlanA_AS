package com.example.plana.function.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.plana.R;
import com.example.plana.base.BaseFragment;
import com.example.plana.base.MainApplication;
import com.example.plana.bean.My;
import com.example.plana.bean.MySubject;
import com.example.plana.bean.Plan;
import com.example.plana.bean.TimerRecorder;
import com.example.plana.bean.Todos;
import com.example.plana.bean.User;
import com.example.plana.bean.rawbean.RawSubject;
import com.example.plana.bean.rawbean.RawTodo;
import com.example.plana.config.Constant;
import com.example.plana.database.DeletedTodosDB;
import com.example.plana.database.TimerDB;
import com.example.plana.database.TodosDB;
import com.example.plana.function.MainActivity;
import com.example.plana.function.setting.AboutActivity;
import com.example.plana.function.setting.InfoModifyActivity;
import com.example.plana.function.setting.TrashBinActivity;
import com.example.plana.function.user.LoginActivity;
import com.example.plana.service.SubjectService;
import com.example.plana.service.TodoService;
import com.example.plana.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @program: PlanA
 * @description:
 */

public class MyPageFragment extends BaseFragment {

    public static final String TAG = "MyPageFragment";

    TextView myName, myEmail;
    Button btLogOut;

    RelativeLayout rlInfoModify;
    RelativeLayout rlUploadToCloud;
    RelativeLayout rlLoadToLocal;
    RelativeLayout rlTrashBin;

    RelativeLayout rlAbout;

    boolean loadTodo, loadSchedule, loadPlan, loadTimer;

    String loadToCloudTitle = "上传到云端";
    String loadToLocalTitle = "覆盖到本地";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        myName = view.findViewById(R.id.my_name);
        myEmail = view.findViewById(R.id.my_email);
        btLogOut = view.findViewById(R.id.btn_logout);

        rlInfoModify = view.findViewById(R.id.rl_personal_Info_modify);
        rlUploadToCloud = view.findViewById(R.id.rl_upload_to_cloud);
        rlLoadToLocal = view.findViewById(R.id.rl_load_from_cloud);
        rlTrashBin = view.findViewById(R.id.rl_trash_bin);
        rlAbout = view.findViewById(R.id.rl_about);

        myPageSetUp();

        rlInfoModify.setOnClickListener(l -> directToInfoModifyActivity());
        rlUploadToCloud.setOnClickListener(v -> showConform(loadToCloudTitle));
        rlLoadToLocal.setOnClickListener(v -> showConform(loadToLocalTitle));
        rlTrashBin.setOnClickListener(v -> directToTrashBinActivity());
        rlAbout.setOnClickListener(l -> directToAboutActivity());

        return view;
    }

    private void showConform(String title) {
        View view = getLayoutInflater().inflate(R.layout.dialog_load_confirm, null);
        TextView text = view.findViewById(R.id.text);
        TextView confirm = view.findViewById(R.id.confirm);
        TextView cancel = view.findViewById(R.id.cancel);
        SwitchCompat switchTodo = view.findViewById(R.id.switch_load_todo);
        SwitchCompat switchSchedule = view.findViewById(R.id.switch_load_schedule);
        SwitchCompat switchPlan = view.findViewById(R.id.switch_load_plan);
        SwitchCompat switchTimer = view.findViewById(R.id.switch_load_timer);

        text.setText("选择" + title + "的内容");
        confirm.setText("确认");
        cancel.setText("取消");

        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT);

        confirm.setOnClickListener(v -> {
            loadTodo = switchTodo.isChecked();
            loadSchedule = switchSchedule.isChecked();
            loadPlan = switchPlan.isChecked();
            loadTimer = switchTimer.isChecked();

            if (loadTodo || loadSchedule || loadPlan || loadTimer) {
                if (title.equals(loadToCloudTitle)) {
                    Toast.makeText(getContext(), "上传中...", Toast.LENGTH_SHORT).show();
                    upLoadDataToCloud();
                } else if (title.equals(loadToLocalTitle)) {
                    Toast.makeText(getContext(), "下载中...", Toast.LENGTH_SHORT).show();
                    loadDataToLocal();
                }
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "至少选择一项", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
    }


    private void upLoadDataToCloud() {
        if (loadTodo) {
            upLoadTodoList();
//            upLoadDeletedTodoList();
        }
        if (loadSchedule) {
            upLoadSubjectList();
//            upLoadScheduleSetting();
        }
//        if (loadPlan) upLoadPlanList();
        if (loadTimer) {
//            upLoadTimerList();
        }
    }

    private void loadDataToLocal() {
        if (loadTodo) {
            downLoadTodoList();
//            upLoadDeletedTodoList();
        }
        if (loadSchedule) {
            downLoadSubjectList();
//            upLoadScheduleSetting();
        }
//        if (loadPlan) upLoadPlanList();
        if (loadTimer) {
//            downLoadTimerList();
        }
    }


    /**
     * 上传计时记录
     * */


    /**
     * 课表上传
     */
    private void upLoadSubjectList() {
        SubjectService subjectService = retrofit.create(SubjectService.class);
        subjectService.deleteSubjectList(My.Account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    for (MySubject subject : My.mySubjects) {
                                        uploadSubject(subjectService, subject);
                                    }
                                    Toast.makeText(getContext(), "subjects ok", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

    private void uploadSubject(SubjectService subjectService, MySubject subject) {
        HashMap<String, String> params = new HashMap<>();
        params.put("owner_id", My.Account.getId() + "");
        params.put("name", subject.getName());
        params.put("room", subject.getRoom());
        params.put("teacher", subject.getTeacher());
        params.put("week_list", subject.getWeekList().toString());  // [2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]
        params.put("start", String.valueOf(subject.getStart()));
        params.put("step", String.valueOf(subject.getStep()));
        params.put("day", String.valueOf(subject.getDay()));

        subjectService.updateSubjectList(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    System.out.println("ok");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

    /**
     * 课表下载
     */
    private void downLoadSubjectList() {
        retrofit.create(SubjectService.class)
                .getSubjectList(My.Account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    String dataString = object.optString("data");
                                    ArrayList<RawSubject> list = new Gson().fromJson(dataString, new TypeToken<ArrayList<RawSubject>>() {
                                    }.getType());
                                    rawSubject2SubjectAndSaveToMy(list);
                                    Toast.makeText(getContext(), "✅ 课程表载入完成", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(Constant.TAG.NETWORK_TAG, "数据导入失败");
                                    Toast.makeText(getContext(), "❌ 数据导入失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                        Toast.makeText(getContext(), "❌ 请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void rawSubject2SubjectAndSaveToMy(ArrayList<RawSubject> list) {
        My.mySubjects.clear();
        for (RawSubject raw : list) {
            MySubject subject = new MySubject(
                    raw.getName(),
                    raw.getRoom(),
                    raw.getTeacher(),
                    getWeekList(raw.getWeek_list()),
                    raw.getStart(),
                    raw.getStep(),
                    raw.getDay(),
                    -1
            );
            My.mySubjects.add(subject);
        }
    }

    private List<Integer> getWeekList(String week_list) {
        week_list = week_list.replaceAll(",", "");
        week_list = week_list.substring(1, week_list.length() - 1);
        String[] split = week_list.split(" ");
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : split) list.add(Integer.parseInt(s));
        return list;
    }


    /**
     * 待办上传
     */
    private void upLoadTodoList() {
        TodoService todoService = retrofit.create(TodoService.class);
        todoService.deleteTodoList(My.Account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    for (Todos todo : My.todosList) {
                                        uploadTodo(todoService, todo);
                                    }
                                    Toast.makeText(getContext(), "todos ok", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

    private void uploadTodo(TodoService todoService, Todos todo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("owner_id", My.Account.getId() + "");
        params.put("content", todo.getContent());
        params.put("done", String.valueOf(todo.isDone()));
        params.put("memo", todo.getMemo());
        params.put("due_date", todo.getDate());
        params.put("due_time", todo.getTime());
        params.put("importance", String.valueOf(todo.getLevel()));

        todoService.updateTodoList(params)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    System.out.println("ok");
                                }
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

    /**
     * 待办下载
     */
    private void downLoadTodoList() {
        retrofit.create(TodoService.class)
                .getTodoList(My.Account)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.optString("result").equals("ok")) {
                                    String dataString = object.optString("data");
                                    ArrayList<RawTodo> list = new ArrayList<>(new Gson().fromJson(dataString, new TypeToken<ArrayList<RawTodo>>() {
                                    }.getType()));
                                    rawTodo2TodoAndSaveToSqlite(list);
                                    Toast.makeText(getContext(), "todos ok", Toast.LENGTH_SHORT).show();
                                } else Log.i(Constant.TAG.NETWORK_TAG, "数据导入失败");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                Log.i(Constant.TAG.NETWORK_TAG, "JSON解析失败：" + e.getMessage());
                            }
                        } else Log.i(Constant.TAG.NETWORK_TAG, "错误代码：" + response.code());

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(Constant.TAG.NETWORK_TAG, "请求失败");
                    }
                });
    }

    private void rawTodo2TodoAndSaveToSqlite(ArrayList<RawTodo> list) {
        TodosDB.dropTable(MainActivity.mainActivity.sqlite);
        My.todosList.clear();
        for (RawTodo raw : list) {
            Todos todo = new Todos(
                    raw.getId(),
                    raw.getContent(),
                    raw.getMemo(),
                    raw.getDone() == 1,
                    raw.getDue_date(),
                    raw.getDue_time(),
                    raw.getImportance()
            );
            My.todosList.add(todo);
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


    private void loadDataToLocal_BackUp() {

        Toast.makeText(getContext(), "载入中...", Toast.LENGTH_SHORT).show();

        SharedPreferencesUtil backUp = SharedPreferencesUtil.init(
                MainApplication.getAppContext(),
                "BACK_UP"
        );

        Gson gson = new Gson();
        String load = backUp.getString("PLAN_LIST");
        if (StringUtils.isNotEmpty(load)) {
            My.plans = gson.fromJson(load, new TypeToken<List<Plan>>() {
            }.getType());
        }
        load = backUp.getString("TODO_LIST");
        if (StringUtils.isNotEmpty(load)) {
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
        if (StringUtils.isNotEmpty(load)) {
            My.mySubjects = gson.fromJson(load, new TypeToken<List<MySubject>>() {
            }.getType());
        }
        load = backUp.getString("RECORDER");
        if (StringUtils.isNotEmpty(load)) {
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
        if (StringUtils.isNotEmpty(load)) {
            ArrayList<Todos> deleteTodos = gson.fromJson(load, new TypeToken<ArrayList<Todos>>() {
            }.getType());
            DeletedTodosDB.dropTable(MainActivity.mainActivity.sqlite);
            for (Todos item : deleteTodos) {
                ContentValues todo_values = new ContentValues();
                todo_values.put(TodosDB._id, item.getId());
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

    private void localToCloud_BackUp() {

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
     * 个人页初始化
     */
    private void myPageSetUp() {
        if (My.Account == null) {
            btLogOut.setText("登录");
            btLogOut.setOnClickListener(l -> {
                startActivity(new Intent(getContext(), LoginActivity.class));
                Log.d(TAG, "进入登录页");
            });
            return;
        }

        User user = My.Account;
        myName.setText(user.getName());
        if (StringUtils.isEmpty(user.getEmail())) {
            myEmail.setText(user.getPhone());
        } else myEmail.setText(My.Account.getEmail());

        btLogOut.setOnClickListener(l -> {
            Toast.makeText(getContext(),
                    "登出", Toast.LENGTH_SHORT).show();
            My.Account = null;
            startActivity(new Intent(getContext(), LoginActivity.class));

            SharedPreferences auto = MainActivity.mainActivity.getSharedPreferences("auto", Context.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.clear();
            autoLogin.apply();

            MainActivity.mainActivity.finish();
            Log.d(TAG, "登出，删除 auto SharedPreferences ");
        });
    }


}
