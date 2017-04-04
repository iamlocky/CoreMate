package core.mate.util;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * {@link SharedPreferences} 辅助类，你可以通过{@link #setThreadSafe(boolean)}
 * 来开闭线程安全的开关，默认情况下打开开关。
 *
 * @author DrkCore
 * @since 2016年2月18日17:56:06
 */
public class ConcurrentPrefHelper extends PrefHelper {

    private volatile boolean threadSafe = true;

    public final boolean isThreadSafe() {
        return threadSafe;
    }

    public final ConcurrentPrefHelper setThreadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
        return this;
    }

    public ConcurrentPrefHelper(String pref) {
        super(pref);
    }

    public ConcurrentPrefHelper(String pref, int mode) {
        super(pref, mode);
    }

    public ConcurrentPrefHelper(SharedPreferences pref) {
        super(pref);
    }

    private final byte[] lock = new byte[0];

    @Override
    public final Map<String, ?> getAll() {
        if (threadSafe) {
            synchronized (lock) {
                return super.getAll();
            }
        }
        return super.getAll();
    }

    @Override
    public final String getString(String key, String defValue) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getString(key, defValue);
            }
        }
        return super.getString(key, defValue);
    }

    @Override
    public final Set<String> getStringSet(String key, Set<String> defValues) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getStringSet(key, defValues);
            }
        }
        return super.getStringSet(key, defValues);
    }

    @Override
    public final int getInt(String key, int defValue) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getInt(key, defValue);
            }
        }
        return super.getInt(key, defValue);
    }

    @Override
    public final long getLong(String key, long defValue) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getLong(key, defValue);
            }
        }
        return super.getLong(key, defValue);
    }

    @Override
    public final float getFloat(String key, float defValue) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getFloat(key, defValue);
            }
        }
        return super.getFloat(key, defValue);
    }

    @Override
    public final boolean getBoolean(String key, boolean defValue) {
        if (threadSafe) {
            synchronized (lock) {
                return super.getBoolean(key, defValue);
            }
        }
        return super.getBoolean(key, defValue);
    }

    @Override
    public final boolean contains(String key) {
        if (threadSafe) {
            synchronized (lock) {
                return super.contains(key);
            }
        }
        return super.contains(key);
    }

    @Override
    public final PrefHelper putString(String key, String value) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putString(key, value);
            }
        }
        return super.putString(key, value);
    }

    @Override
    public final PrefHelper putStringSet(String key, Set<String> values) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putStringSet(key, values);
            }
        }
        return super.putStringSet(key, values);
    }

    @Override
    public final PrefHelper putBoolean(String key, boolean value) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putBoolean(key, value);
            }
        }
        return super.putBoolean(key, value);
    }

    @Override
    public final PrefHelper putInt(String key, int value) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putInt(key, value);
            }
        }
        return super.putInt(key, value);
    }

    @Override
    public final PrefHelper putLong(String key, long value) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putLong(key, value);
            }
        }
        return super.putLong(key, value);
    }

    @Override
    public final PrefHelper putFloat(String key, float value) {
        if (threadSafe) {
            synchronized (lock) {
                return super.putFloat(key, value);
            }
        }
        return super.putFloat(key, value);
    }

    @Override
    public final PrefHelper remove(String key) {
        if (threadSafe) {
            synchronized (lock) {
                return super.remove(key);
            }
        }
        return super.remove(key);
    }

    @Override
    public final PrefHelper clear() {
        if (threadSafe) {
            synchronized (lock) {
                return super.clear();
            }
        }
        return super.clear();
    }
}
