package core.mate.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author DrkCore
 * @since 2017/1/3
 */
public class CoreReceiver extends BroadcastReceiver {

    public interface Receiver {

        void onReceive(Context context, Intent intent);

    }

    private final IntentFilter filter;
    private final boolean local;

    public IntentFilter getFilter() {
        return filter;
    }

    public boolean isLocal() {
        return local;
    }

    public CoreReceiver(IntentFilter filter, boolean local) {
        this.filter = filter;
        this.local = local;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
