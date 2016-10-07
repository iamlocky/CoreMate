package core.mate.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.util.BroadcastUtil;

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

        if (receivers != null) {
            for (Object[] item : receivers) {
                BroadcastUtil.unregisterReceiver((BroadcastReceiver) item[0]);
            }
        }
    }

    /*广播*/

    private List<Object[]> receivers;

    protected final void addReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (receiver == null || filter == null) {
            throw new IllegalArgumentException();
        }
        if (receivers == null) {
            receivers = new ArrayList<>();
        }
        receivers.add(new Object[]{receiver, filter});
        BroadcastUtil.registerReceiver(receiver,filter);
    }

	/*Clearable*/

    private ClearableHolder clearableHolder;

    public final <T> T addClearableEx(T t) {
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
    public final void addClearable(Clearable clearable) {
        if (clearableHolder == null) {
            clearableHolder = new ClearableHolder();
        }
        clearableHolder.add(clearable);
    }

    /**
     * clear所有保存着的引用。在{@link #onDestroy()}时自动回调。
     */
    public final void clearAllClearable() {
        if (clearableHolder != null) {
            clearableHolder.clear();
        }
    }

}
