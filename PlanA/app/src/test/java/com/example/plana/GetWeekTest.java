package com.example.plana;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @program: PlanA
 * @description:
 */
public class GetWeekTest {

    @Test
    public void name() {
        String week_list = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]";
        week_list = week_list.replaceAll(",", "");
        week_list = week_list.substring(1, week_list.length()-1);
        String[] split = week_list.split(" ");
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : split) {
            list.add(Integer.parseInt(s));
            System.out.println(Integer.parseInt(s));
        }
        System.out.println(list);

    }
}
