package core.mate.util;

import android.content.Context;
import android.content.SharedPreferences;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2017-04-03
 */
public final class CoreConfig {

    private static final SharedPreferences PREF;

    static {
        PREF = Core.getInstance().getAppContext().getSharedPreferences("CoreConfig", Context.MODE_PRIVATE);
    }

    public static synchronized void put(String key, boolean val) {
        PREF.edit().putBoolean(key, val).apply();
    }

    public static synchronized boolean get(String key, boolean defVal) {
        return PREF.getBoolean(key, defVal);
    }

    public static synchronized void put(String key, int val) {
        PREF.edit().putInt(key, val).apply();
    }

    public static synchronized int get(String key, int defVal) {
        return PREF.getInt(key, defVal);
    }

    public static synchronized void put(String key, float val) {
        PREF.edit().putFloat(key, val).apply();
    }

    public static synchronized float get(String key, float defVal) {
        return PREF.getFloat(key, defVal);
    }

    public static synchronized void put(String key, long val) {
        PREF.edit().putLong(key, val).apply();
    }

    public static synchronized long get(String key, long defVal) {
        return PREF.getLong(key, defVal);
    }

    public static synchronized void put(String key, String val) {
        PREF.edit().putString(key, val).apply();
    }

    public static synchronized String get(String key, String defVal) {
        return PREF.getString(key, defVal);
    }

}
