package com.example.plana;

import android.content.res.Resources;

import org.junit.Test;
import org.xmind.core.Core;
import org.xmind.core.CoreException;
import org.xmind.core.IEncryptionHandler;
import org.xmind.core.IWorkbook;
import org.xmind.core.IWorkbookBuilder;
import org.xmind.core.io.ByteArrayStorage;

import java.io.IOException;
import java.io.InputStream;

/**
 * @program: PlanA
 * @description:
 */
public class XMindParseTest {

    @Test
    public void loadXMind() throws CoreException, IOException {

        String url = Resources.getSystem().getResourcePackageName(R.raw.cn_0);

        System.out.println(url);

        String pathUrl = "android.resource://" + url + "/" + R.raw.cn_0;
        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IEncryptionHandler iench = () -> "privet";

        IWorkbook workbook = builder.loadFromPath(pathUrl, new ByteArrayStorage(), iench);


    }
}
