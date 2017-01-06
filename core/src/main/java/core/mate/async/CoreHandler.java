package core.mate.async;

import android.os.Handler;

/**
 * @author DrkCore
 * @since 2017/1/6
 */
public class CoreHandler extends Handler implements Clearable {

    @Override
    public boolean isCleared() {
        return false;
    }

    @Override
    public void clear() {
        removeCallbacksAndMessages(null);
    }
}
