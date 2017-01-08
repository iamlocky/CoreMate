package core.mate.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import core.mate.util.BroadcastUtil;

/**
 * @author DrkCore
 * @since 2017/1/3
 */
public class CoreReceiver extends BroadcastReceiver {

    private final boolean local;
    private final IntentFilter filter;

    public boolean isLocal() {
        return local;
    }

    public IntentFilter getFilter() {
        return filter;
    }

    public CoreReceiver(boolean local, String... actions) {
        this(local, BroadcastUtil.createFilter(actions));
    }

    public CoreReceiver(boolean local, IntentFilter filter) {
        this.local = local;
        this.filter = filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
