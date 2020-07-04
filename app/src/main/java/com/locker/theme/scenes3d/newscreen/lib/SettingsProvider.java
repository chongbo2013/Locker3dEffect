package com.locker.theme.scenes3d.newscreen.lib;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xff on 2017/3/14.
 */

public class SettingsProvider {
    public static final String SETTINGS_KEY = "com_locker_theme_cube3d_preferences";

    //是否静音
    public static final String SETTINGS_IS_MUTE = "setting_is_mute";


    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    public static int getIntCustomDefault(Context context, String key, int def) {
        return get(context).getInt(key, def);
    }

    public static int getInt(Context context, String key, int resource) {
        return getIntCustomDefault(context, key, context.getResources().getInteger(resource));
    }

    public static long getLongCustomDefault(Context context, String key, long def) {
        return get(context).getLong(key, def);
    }

    public static long getLong(Context context, String key, int resource) {
        return getLongCustomDefault(context, key, context.getResources().getInteger(resource));
    }

    public static boolean getBooleanCustomDefault(Context context, String key, boolean def) {
        return get(context).getBoolean(key, def);
    }

    public static boolean getBoolean(Context context, String key, int resource) {
        return getBooleanCustomDefault(context, key, context.getResources().getBoolean(resource));
    }

    public static String getStringCustomDefault(Context context, String key, String def) {
        return get(context).getString(key, def);
    }

    public static String getString(Context context, String key, int resource) {
        return getStringCustomDefault(context, key, context.getResources().getString(resource));
    }

    public static void putString(Context context, String key, String value) {
        get(context).edit().putString(key, value).commit();
    }

    public static void putInt(Context context, String key, int value) {
        get(context).edit().putInt(key, value).commit();
    }

    public static boolean changeBoolean(Context context, String key, int defaultRes) {
        boolean def = context.getResources().getBoolean(defaultRes);
        boolean val = !SettingsProvider.getBooleanCustomDefault(context, key, def);
        putBoolean(context, key, val);
        return val;
    }

    public static void putBoolean(Context context, String key, int res) {
        boolean val = context.getResources().getBoolean(res);
        putBoolean(context, key, val);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        get(context).edit().putBoolean(key, value).commit();
    }
}
