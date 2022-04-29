package com.example.plana.base;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: PlanA
 * @description: BaseFragment extends Fragment
 */
public class BaseFragment extends Fragment {
    Map<String, String> params = new HashMap<>();
    Map<String, String> header_params = new HashMap<>();


    public void request(String url) {

    }


    public void sendNotification(String token, String title, String message) {

    }


    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }





}
