package com.example.plana;

import com.example.plana.utils.TimeCalcUtil;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @program: PlanA
 * @description:
 */
public class TimeCalcUtilTest {

    @Test
    public void name() {

        System.out.println(TimeCalcUtil.calcEndTime(23, 32, 120));

        System.out.println(TimeCalcUtil.format02d(9));
        System.out.println(TimeCalcUtil.format02d(20));


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 15);      // 24
        cal.set(Calendar.HOUR, 7);              // 12
        cal.set(Calendar.MINUTE, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        System.out.println(sdf.format(cal.getTime()));

    }
}
