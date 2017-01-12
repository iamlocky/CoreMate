package core.mate.util;

import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * 简单的回调接口，用于异步编程。配合lambda表达式一同食用有奇效。
 * <p>
 * 如果你需要回调两个参数的话可以试试{@link android.support.v4.util.Pair}，
 * 超出两个的话建议使用数组或者定义一个Bean。
 *
 * @author DrkCore
 * @since 2016/12/22
 */
public abstract class SimpleCallback<Result> implements Callback<Result> {

    public void onCall(Result result) {
        if (result != null) {
            onNotNull(result);
        } else {
            onNull();
        }
    }

    public void onNotNull(@NonNull Result result) {

    }

    public void onNull() {

    }

    public static <Result> void call(Result result, Callback<Result>... callbacks) {
        for (int i = 0, len = DataUtil.getSize(callbacks); i < len; i++) {
            if (callbacks[i] != null) {
                callbacks[i].onCall(result);
            }
        }
    }

    public static <Result> void call(Result result, Collection<Callback<Result>> callbacks) {
        if (!DataUtil.isEmpty(callbacks)) {
            for (Callback<Result> callback : callbacks) {
                if (callback != null) {
                    callback.onCall(result);
                }
            }
        }
    }

}
