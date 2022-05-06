package com.example.plana.function.schedule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.plana.R;
import com.example.plana.base.BaseActivity;
import com.example.plana.base.MainApplication;
import com.example.plana.function.MainActivity;
import com.example.plana.utils.SharedPreferencesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: PlanA
 * @description:
 */
public class LoadScheduleActivity extends BaseActivity {

    private static final String TAG = "LoadScheduleActivity";

    WebView webView;
    TextView tvImport;

    TextView tvWebTitle;
    ImageButton btBack;
    ImageButton btRefresh;
    String parseHtmlJS;
    String URL = "https://jx.sspu.edu.cn/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_schedule);
        webView = findViewById(R.id.classWeb);
        tvImport = findViewById(R.id.tv_button);
        tvWebTitle = findViewById(R.id.tv_web_title);
        btBack = findViewById(R.id.bt_load_schedule_back);
        btRefresh = findViewById(R.id.bt_load_schedule_refresh);

        btBack.setOnClickListener(v -> finish());

        try {
            openWeb();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btRefresh.setOnClickListener(l-> {
            try {
                openWeb();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tvImport.setOnClickListener(v -> importSubject());
    }


    /**
     * 打开网页
     *
     * @throws IOException
     */
    public void openWeb() throws IOException {

        // webView 设置
        WebSettings ws = webView.getSettings();
        // 支持js
        ws.setJavaScriptEnabled(true);    // 允许js
        ws.setJavaScriptCanOpenWindowsAutomatically(true);  // 允许js打开新窗口
        // 设置缓存模式
//        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 缩放操作
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);   // 开启缩放
        ws.setDisplayZoomControls(false);  // 隐藏原生的缩放控件
        // 自适应屏幕
        ws.setUseWideViewPort(true);       // 自适应屏幕
        ws.setLoadWithOverviewMode(true);  // 缩放至屏幕的大小

        // 设置浏览器标识，以pc模式打开网页
        ws.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        ws.setAllowFileAccess(true);       // 可以访问文件

        // 读取 assets 里的js文件
        InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("parseHtml.js"));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line;
        parseHtmlJS = "";
        while ((line = bufReader.readLine()) != null) {
            parseHtmlJS += line;
        }
//        Log.e(TAG, "parseHtmlJS: " + parseHtmlJS);

        // 加载网页
//        webView.loadUrl(parseHtmlJS);
        webView.loadUrl(URL);
        webView.setWebViewClient(new WebViewClient() {
            // js 注入
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webView.evaluateJavascript(parseHtmlJS, null);
            }
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    // 设置标题
                    // getSupportActionBar().setTitle(title);
                    tvWebTitle.setText(title);
                    Log.e(TAG, "title" + title);
                }
            }
        });
    }

    /**
     * 导入课程保存为json字符串
     */
    public void importSubject() {
        // 通过Handler发送消息
        webView.post(() -> {
            // 注意调用的JS方法名要对应上
            // 调用javascript的parseHtml()方法
            // webView.loadUrl("javascript:parseHtml()");
            webView.evaluateJavascript("javascript:parseHtml()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                    Log.e(TAG, value);

                    // 此处为 js 返回的结果
//                                SharedPreferences sp = getSharedPreferences("COURSE_DATA", Context.MODE_PRIVATE);//创建sp对象
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.clear();
//                                editor.putString("HTML_TO_SUBJECT", value); //存入json串
//                                editor.commit();//提交


//                    SharedPreferencesUtil.init(MainApplication.getAppContext(), "COURSE_DATA").putString("HTML_TO_SUBJECT", value);
//                    SharedPreferencesUtil.init(MainApplication.getAppContext(), "COURSE_DATA").remove("SUBJECT_LIST");
//                    Log.e(TAG, "HTML_TO_SUBJECT: " + value);
//
//                    Intent intent = new Intent(LoadScheduleActivity.this, MainActivity.class);
//                    if (MainActivity.mainActivity != null) {
//                        MainActivity.mainActivity.finish(); // 销毁MainActivity
//                    }
//                    startActivity(intent);
//                    finish();
                }
            });
        });
    }

}
