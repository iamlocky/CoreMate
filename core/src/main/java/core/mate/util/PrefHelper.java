package core.mate.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2016年2月18日17:56:06
 */
public class PrefHelper {

    private final SharedPreferences pref;
    private volatile boolean applyOrCommit = true;

    public final PrefHelper setApplyOrCommit(boolean applyOrCommit) {
        this.applyOrCommit = applyOrCommit;
        return this;
    }

    public PrefHelper(String pref) {
        this(pref, Context.MODE_PRIVATE);
    }

    public PrefHelper(String pref, int mode) {
        this(Core.getInstance().getAppContext().getSharedPreferences(pref, mode));
    }

    public PrefHelper(SharedPreferences pref) {
        this.pref = pref;
    }

    /*数据读写*/

    public Map<String, ?> getAll() {
        return pref.getAll();
    }

    public String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return pref.getStringSet(key, defValues);
    }

    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public boolean contains(String key) {
        return pref.contains(key);
    }

    public PrefHelper putString(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        submitEdit(editor);
        return this;
    }

    public PrefHelper putStringSet(String key, Set<String> values) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, values);
        submitEdit(editor);
        return this;
    }

    public PrefHelper putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        submitEdit(editor);
        return this;
    }

    public PrefHelper putInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        submitEdit(editor);
        return this;
    }

    public PrefHelper putLong(String key, long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        submitEdit(editor);
        return this;
    }

    public PrefHelper putFloat(String key, float value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        submitEdit(editor);
        return this;
    }

    public PrefHelper remove(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        submitEdit(editor);
        return this;
    }

    public PrefHelper clear() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        submitEdit(editor);
        return this;
    }

    private void submitEdit(SharedPreferences.Editor editor) {
        if (applyOrCommit) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

}
