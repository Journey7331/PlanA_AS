package com.example.plana.activity;

import androidx.test.rule.ActivityTestRule;

import com.example.plana.R;

import org.junit.Rule;
import org.junit.Test;
import org.xmind.core.Core;
import org.xmind.core.CoreException;
import org.xmind.core.IEncryptionHandler;
import org.xmind.core.IWorkbook;
import org.xmind.core.IWorkbookBuilder;
import org.xmind.core.io.ByteArrayStorage;

import java.io.IOException;

public class PlanDetailActivityTest {

    @Rule
    public ActivityTestRule<PlanDetailActivity> activityTestRule =
            new ActivityTestRule<>(PlanDetailActivity.class);


    @Test
    public void onCreate() throws CoreException, IOException {

        PlanDetailActivity activity = activityTestRule.getActivity();


        String packageName = activity.getResources().getResourcePackageName(R.raw.cn_0);
        System.out.println(packageName);
        String pathUrl = "android.resource://" + packageName + "/" + R.raw.cn_0;

        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IEncryptionHandler iench = () -> "privet";
        IWorkbook workbook = builder.loadFromPath(pathUrl, new ByteArrayStorage(), iench);

    }
}