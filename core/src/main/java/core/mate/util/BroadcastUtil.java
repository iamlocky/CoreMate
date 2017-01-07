package core.mate.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import core.mate.Core;

/**
 * @author DrkCore
 * @since 2016-10-02
 */
public class BroadcastUtil {

    public static abstract class Wrapper {

        public abstract void register(BroadcastReceiver receiver, IntentFilter filter);

        public abstract void unregister(BroadcastReceiver receiver);

        public void send(String action) {
            send(new Intent(action));
        }

        public abstract void send(Intent intent);
    }

    private static class LocalWrapper extends Wrapper {

        private static volatile LocalWrapper instance = null;

        private LocalWrapper() {
        }

        public static LocalWrapper getInstance() {
            if (instance == null) {
                synchronized (LocalWrapper.class) {
                    if (instance == null) {
                        instance = new LocalWrapper();
                    }
                }
            }
            return instance;
        }

        private LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(Core.getInstance().getAppContext());

        @Override
        public void register(BroadcastReceiver receiver, IntentFilter filter) {
            mgr.registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(BroadcastReceiver receiver) {
            mgr.unregisterReceiver(receiver);
        }

        @Override
        public void send(Intent intent) {
            mgr.sendBroadcast(intent);
        }

    }

    private static class GlobalManager extends Wrapper {

        private static volatile GlobalManager instance = null;

        private GlobalManager() {
        }

        public static GlobalManager getInstance() {
            if (instance == null) {
                synchronized (GlobalManager.class) {
                    if (instance == null) {
                        instance = new GlobalManager();
                    }
                }
            }
            return instance;
        }

        private final Context context = Core.getInstance().getAppContext();

        @Override
        public void register(BroadcastReceiver receiver, IntentFilter filter) {
            context.registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(BroadcastReceiver receiver) {
            context.unregisterReceiver(receiver);
        }

        @Override
        public void send(Intent intent) {
            context.sendBroadcast(intent);
        }
    }

    public static Wrapper getManager(boolean local) {
        return local ? LocalWrapper.getInstance() : GlobalManager.getInstance();
    }

}
