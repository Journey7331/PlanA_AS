package com.example.plana.function.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.plana.function.MainActivity;
import com.example.plana.base.BaseFragment;
import com.example.plana.bean.My;
import com.example.plana.function.setting.InfoModifyActivity;
import com.example.plana.function.user.AboutActivity;
import com.example.plana.function.user.LoginActivity;

/**
 * @program: PlanA
 * @description:
 */

public class MyPageFragment extends BaseFragment {

    public static final String TAG = "MyPageFragment";

    TextView myName, myEmail;
    Button btLogOut;

    RelativeLayout rlInfoModify;
    RelativeLayout rlNotifyModify;
    RelativeLayout rlUploadToCloud;
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
        rlNotifyModify = view.findViewById(R.id.rl_notify_modify);
        rlUploadToCloud = view.findViewById(R.id.rl_upload_to_cloud);
        rlTrashBin = view.findViewById(R.id.rl_trash_bin);

        rlAbout = view.findViewById(R.id.rl_about);

        myPageSetUp();

        rlInfoModify.setOnClickListener(l -> directToInfoModifyActivity());


        rlAbout.setOnClickListener(l-> directToAboutActivity());

        return view;
    }


    /**
     * 跳转到关于界面
     * */
    private void directToAboutActivity() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到个人信息修改页面
     * */
    public void directToInfoModifyActivity() {
        Intent intent = new Intent(getContext(), InfoModifyActivity.class);
        startActivity(intent);
    }


    /**
     * 初始化
     * */
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
                MainActivity.mainActivity.finish();

                SharedPreferences auto = MainActivity.mainActivity.getSharedPreferences("auto", Context.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.clear();
                autoLogin.apply();

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
