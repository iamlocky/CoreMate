package core.mate.util;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * 带加密的{@link SharedPreferences}配置处理文件
 *
 * @author DrkCore
 * @since 2016年2月18日17:56:06
 */
public class PrefHelper {

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final boolean applyOrCommit;

    public PrefHelper(SharedPreferences pref) {
        this(pref, true);
    }

    public PrefHelper(SharedPreferences pref, boolean applyOrCommit) {
        this.pref = pref;
        this.editor = pref.edit();
        this.applyOrCommit = applyOrCommit;
    }

    /*数据读写*/

    public final Map<String, ?> getAll() {
        return pref.getAll();
    }

    public final String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public final Set<String> getStringSet(String key, Set<String> defValues) {
        return pref.getStringSet(key, defValues);
    }

    public final int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public final long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public final float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public final boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public final boolean contains(String key) {
        return pref.contains(key);
    }

    public final PrefHelper putString(String key, String value) {
        editor.putString(key, value);
        submitEdit();
        return this;
    }

    public PrefHelper putStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        submitEdit();
        return this;
    }

    public final PrefHelper putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        submitEdit();
        return this;
    }

    public final PrefHelper putInt(String key, int value) {
        editor.putInt(key, value);
        submitEdit();
        return this;
    }

    public final PrefHelper putLong(String key, long value) {
        editor.putLong(key, value);
        submitEdit();
        return this;
    }

    public final PrefHelper putFloat(String key, float value) {
        editor.putFloat(key, value);
        submitEdit();
        return this;
    }

    public final PrefHelper remove(String key) {
        editor.remove(key);
        submitEdit();
        return this;
    }

    public final PrefHelper clear() {
        editor.clear();
        submitEdit();
        return this;
    }

    private void submitEdit() {
        if (applyOrCommit) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

}
