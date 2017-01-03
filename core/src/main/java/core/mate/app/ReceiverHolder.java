package core.mate.app;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * @author DrkCore
 * @since 2017/1/3
 */
class ReceiverHolder {

    final BroadcastReceiver receiver;
    final IntentFilter filter;
    final boolean local;

    ReceiverHolder(BroadcastReceiver receiver, IntentFilter filter, boolean local) {
        this.receiver = receiver;
        this.filter = filter;
        this.local = local;
    }
}
