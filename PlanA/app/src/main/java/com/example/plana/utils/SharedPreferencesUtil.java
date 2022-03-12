package com.example.plana.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.StringRes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: PlanA
 * @description: 封装 SharePreferences, 读取保存数据
 */
public class SharedPreferencesUtil {

    private static final String TAG = "SharedPreferencesUtil";

    private static SharedPreferencesUtil sharedPreferencesUtil;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String DEFAULT_SP_NAME = "SharedData";
    private static final int DEFAULT_INT = 0;
    private static final float DEFAULT_FLOAT = 0.0f;
    private static final String DEFAULT_STRING = "";
    private static final boolean DEFAULT_BOOLEAN = false;
    private static final Set<String> DEFAULT_STRING_SET = new HashSet<>(0);

    private static String curSPName = DEFAULT_SP_NAME;
    private static Context context;

    private SharedPreferencesUtil(Context context) {
        this(context, DEFAULT_SP_NAME);
    }

    private SharedPreferencesUtil(Context context, String spName) {
        SharedPreferencesUtil.context = context.getApplicationContext();
        sharedPreferences = SharedPreferencesUtil.context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        curSPName = spName;
        Log.i(TAG, "SharedPreferencesUtil: " + curSPName);
    }

    public static SharedPreferencesUtil init(Context context) {
        if (sharedPreferencesUtil == null || !curSPName.equals(DEFAULT_SP_NAME)) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sharedPreferencesUtil;
    }

    public static SharedPreferencesUtil init(Context context, String spName) {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context, spName);
        } else if (!spName.equals(curSPName)) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context, spName);
        }
        return sharedPreferencesUtil;
    }

    public SharedPreferencesUtil put(@StringRes int key, Object value) {
        return put(context.getString(key), value);
    }

    public SharedPreferencesUtil put(String key, Object value) {

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.apply();
        return sharedPreferencesUtil;
    }

    public Object get(@StringRes int key, Object defaultObject) {
        return get(context.getString(key), defaultObject);
    }

    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (int) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (long) defaultObject);
        }
        return null;
    }

    public SharedPreferencesUtil putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
        return this;
    }

    public SharedPreferencesUtil putInt(@StringRes int key, int value) {
        return putInt(context.getString(key), value);
    }

    public int getInt(@StringRes int key) {
        return getInt(context.getString(key));
    }

    public int getInt(@StringRes int key, int defValue) {
        return getInt(context.getString(key), defValue);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }


    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public SharedPreferencesUtil putFloat(@StringRes int key, float value) {
        return putFloat(context.getString(key), value);
    }

    public SharedPreferencesUtil putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
        return sharedPreferencesUtil;
    }

    public float getFloat(String key) {
        return getFloat(key, DEFAULT_FLOAT);
    }

    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public float getFloat(@StringRes int key) {
        return getFloat(context.getString(key));
    }

    public float getFloat(@StringRes int key, float defValue) {
        return getFloat(context.getString(key), defValue);
    }

    public SharedPreferencesUtil putLong(@StringRes int key, long value) {
        return putLong(context.getString(key), value);
    }

    public SharedPreferencesUtil putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
        return sharedPreferencesUtil;
    }

    public long getLong(String key) {
        return getLong(key, DEFAULT_INT);
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public long getLong(@StringRes int key) {
        return getLong(context.getString(key));
    }

    public long getLong(@StringRes int key, long defValue) {
        return getLong(context.getString(key), defValue);
    }

    public SharedPreferencesUtil putString(@StringRes int key, String value) {
        return putString(context.getString(key), value);
    }

    public SharedPreferencesUtil putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
        return sharedPreferencesUtil;
    }

    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public String getString(@StringRes int key) {
        return getString(context.getString(key), DEFAULT_STRING);
    }

    public String getString(@StringRes int key, String defValue) {
        return getString(context.getString(key), defValue);
    }

    public SharedPreferencesUtil putBoolean(@StringRes int key, boolean value) {
        return putBoolean(context.getString(key), value);
    }

    public SharedPreferencesUtil putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
        return sharedPreferencesUtil;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public boolean getBoolean(@StringRes int key) {
        return getBoolean(context.getString(key));
    }

    public boolean getBoolean(@StringRes int key, boolean defValue) {
        return getBoolean(context.getString(key), defValue);
    }

    public SharedPreferencesUtil putStringSet(@StringRes int key, Set<String> value) {
        return putStringSet(context.getString(key), value);
    }

    public SharedPreferencesUtil putStringSet(String key, Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            editor.putStringSet(key, value);
            editor.apply();
        }
        return sharedPreferencesUtil;
    }

    public Set<String> getStringSet(String key) {
        return getStringSet(key, DEFAULT_STRING_SET);
    }


    public Set<String> getStringSet(String key, Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            return DEFAULT_STRING_SET;
        }
    }

    public Set<String> getStringSet(@StringRes int key) {
        return getStringSet(context.getString(key));
    }

    public Set<String> getStringSet(@StringRes int key, Set<String> defValue) {
        return getStringSet(context.getString(key), defValue);
    }


    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public boolean contains(@StringRes int key) {
        return contains(context.getString(key));
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public SharedPreferencesUtil remove(@StringRes int key) {
        return remove(context.getString(key));
    }

    public SharedPreferencesUtil remove(String key) {
        editor.remove(key);
        editor.apply();
        return sharedPreferencesUtil;
    }

    public SharedPreferencesUtil clear() {
        editor.clear();
        editor.apply();
        return sharedPreferencesUtil;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
