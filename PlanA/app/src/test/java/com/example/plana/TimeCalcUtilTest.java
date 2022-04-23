package com.example.plana;

import com.example.plana.utils.TimeCalcUtil;

import org.junit.Test;

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

    }
}
