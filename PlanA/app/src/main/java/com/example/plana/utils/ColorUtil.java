package com.example.plana.utils;

import android.content.res.ColorStateList;

/**
 * @program: PlanA
 * @description:
 */
public class ColorUtil {


    /**
     * 重写 CheckBox 的颜色
     */
    static public ColorStateList ColorStateList(int level) {
        // 【 FF -> transparency 】
        int[] levelColors = new int[]{
                0xFFB7EFC5, 0xFF92E6A7, 0xFF6EDE8A, 0xFF4AD66D, 0xFF2DC653,
                0xFF25A244, 0xFF208B3A, 0xFF1A7431, 0xFF155D27, 0xFF10451D,
        };
        int color = levelColors[level - 1];
        int[] colors = new int[]{color, color, color, color, color, color};
        int[][] states = new int[6][];

        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    static public ColorStateList ColorDefaultList(int color) {
        int[] colors = new int[]{color, color, color, color, color, color};
        int[][] states = new int[6][];

        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

}
