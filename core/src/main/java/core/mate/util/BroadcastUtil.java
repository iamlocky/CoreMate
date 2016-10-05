package core.mate.util;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2016-10-02
 */
public class BroadcastUtil {

    /*广播*/

    private static final LocalBroadcastManager BROADCAST_MANAGER =
            LocalBroadcastManager.getInstance(Core.getInstance().getAppContext());

    public static void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        BROADCAST_MANAGER.registerReceiver(receiver, filter);
    }

    public static void unregisterReceiver(BroadcastReceiver receiver) {
        BROADCAST_MANAGER.unregisterReceiver(receiver);
    }

    public static void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    public static void sendBroadcast(Intent intent) {
        BROADCAST_MANAGER.sendBroadcast(intent);
    }

    public static void sendSerializable(Serializable serializable) {
        sendSerializable(serializable.getClass(),serializable);
    }

    public static void sendSerializable(Class clz, @Nullable Serializable serializable) {
        String pkgName = clz.getCanonicalName();

        Intent intent = new Intent(pkgName);
        intent.putExtra(pkgName, serializable);
        sendBroadcast(intent);
    }

    public static <T extends Serializable> T getSerializable(Intent intent, Class clz) {
        String pkgPath = clz.getCanonicalName();
        return (T) intent.getSerializableExtra(pkgPath);
    }

}
