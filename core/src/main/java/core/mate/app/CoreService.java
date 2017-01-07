package core.mate.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.util.BroadcastUtil;
import core.mate.util.DataUtil;

public abstract class CoreService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAllClearable();

        for (int i = 0, len = DataUtil.getSize(fullReceivers); i < len; i++) {
            ReceiverHolder item = fullReceivers.get(i);
            unregisterReceiver(item.receiver, item.local);
        }
    }

    /*广播*/

    private List<ReceiverHolder> fullReceivers;

    /**
     * 注册长时间监听的广播。该广播会在调用该方法时自动注册，并在{@link #onDestroy()}中注销。
     * <p>
     * 默认注册为全局广播。
     *
     * @param receiver
     * @param filter
     */
    public void addFullReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        addFullReceiver(receiver, filter, false);
    }

    /**
     * 注册长时间监听的广播。该广播会在调用该方法时自动注册，并在{@link #onDestroy()}中注销。
     *
     * @param receiver
     * @param filter
     * @param local    是否通过{@link LocalBroadcastManager}注册为本地广播。
     *                 如果你要接受系统的广播的话请将之设为false，否则可能会出现无法响应的问题。
     */
    public void addFullReceiver(BroadcastReceiver receiver, IntentFilter filter, boolean local) {
        if (receiver == null || filter == null) {
            throw new IllegalArgumentException();
        }
        if (fullReceivers == null) {
            fullReceivers = new ArrayList<>();
        }
        fullReceivers.add(new ReceiverHolder(receiver, filter, local));
        registerReceiver(receiver, filter, local);
    }

    /**
     * 注册广播。
     *
     * @param receiver
     * @param filter
     * @param local    是否通过{@link LocalBroadcastManager}注册为本地广播。
     *                 如果你要接受系统的广播的话请将之设为false，否则可能会出现无法响应的问题。
     */
    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter, boolean local) {
        BroadcastUtil.getManager(local).register(receiver, filter);
    }

    /**
     * 注销广播。
     *
     * @param receiver
     * @param local    该广播是否是通过{@link LocalBroadcastManager}注册的本地广播。
     *                 如果和注册时不一致的话，会无法注销广播。
     */
    public void unregisterReceiver(BroadcastReceiver receiver, boolean local) {
        BroadcastUtil.getManager(local).unregister(receiver);
    }

	/*Clearable*/

    private ClearableHolder clearableHolder;

    public <T> T addClearableEx(T t) {
        if (t instanceof Clearable) {
            addClearable((Clearable) t);
        } else if (t instanceof AsyncTask) {
            //如果是原生的AsyncTask的话包裹起来
            //CoreTask已经实现了Clearable接口所以无需担心
            AsyncTask task = (AsyncTask) t;
            addClearable(new ClearableWrapper(task));
        }
        return t;
    }

    /**
     * 保存clearable的弱引用
     *
     * @param clearable
     */
    public void addClearable(Clearable clearable) {
        if (clearableHolder == null) {
            clearableHolder = new ClearableHolder();
        }
        clearableHolder.add(clearable);
    }

    /**
     * clear所有保存着的引用。在{@link #onDestroy()}时自动回调。
     */
    public void clearAllClearable() {
        if (clearableHolder != null) {
            clearableHolder.clear();
        }
    }

}
