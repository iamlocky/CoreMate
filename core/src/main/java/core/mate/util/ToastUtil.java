package core.mate.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2016年5月28日16:37:55
 */
public class ToastUtil {

    public static Toast show(@StringRes int resId) {
        return show(resId, Toast.LENGTH_SHORT);
    }

    public static Toast show(CharSequence tip) {
        return show(tip, Toast.LENGTH_SHORT);
    }

    public static Toast showLong(@StringRes int resId) {
        return show(resId, Toast.LENGTH_LONG);
    }

    public static Toast showLong(CharSequence tip) {
        return show(tip, Toast.LENGTH_LONG);
    }

    public static Toast show(@StringRes int resId, int duration) {
        return show(ContextUtil.getString(resId), duration);
    }

    public static Toast show(CharSequence tip, int duration) {
        Toast toast = Toast.makeText(Core.getInstance().getAppContext(), tip, duration);
        toast.show();
        return toast;
    }

}
