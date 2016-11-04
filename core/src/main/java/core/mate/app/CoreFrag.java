package core.mate.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.util.BroadcastUtil;

/**
 * 规范化fragment执行顺序的抽象fragment基类。
 *
 * @author DrkCore
 * @since 2015年5月17日23:09:05
 */
public abstract class CoreFrag extends Fragment {

    /* 继承 */

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //该部分代码来自博客：http://www.jianshu.com/p/c12a98a36b2b
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefreshOnResume()) {
            refresh();
        }

        if (resumeReceivers != null) {
            for (Object[] item : resumeReceivers) {
                BroadcastUtil.registerReceiver((BroadcastReceiver) item[0], (IntentFilter) item[1]);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (clearAllOnPauseEnable) {
            clearAllClearable();
        }

        if (resumeReceivers != null) {
            for (Object[] item : resumeReceivers) {
                BroadcastUtil.unregisterReceiver((BroadcastReceiver) item[0]);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAllClearable();

        if (fullReceivers != null) {
            for (Object[] item : fullReceivers) {
                BroadcastUtil.unregisterReceiver((BroadcastReceiver) item[0]);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (refreshOnVisibleEnable && !hidden && needRefreshOnResume()) {
            refresh();
        }
    }

	/* 刷新 */

    private RefreshRate refreshRate = RefreshRate.ONCE;
    private boolean refreshOnVisibleEnable = true;
    private boolean onceRefreshed;

    public CoreFrag setRefreshOnVisibleEnable(boolean refreshOnVisibleEnable) {
        this.refreshOnVisibleEnable = refreshOnVisibleEnable;
        return this;
    }

    /**
     * 获取刷新频率，默认返回{@link RefreshRate#ONCE}。
     *
     * @return
     */
    public RefreshRate getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(RefreshRate refreshRate) {
        this.refreshRate = refreshRate;
    }

    private boolean needRefreshOnResume() {
        boolean needRefresh = true;
        refreshRate = refreshRate != null ? refreshRate : RefreshRate.ONCE;
        switch (refreshRate) {
            case ALWAYS:
                break;

            case ONCE:
                if (onceRefreshed) {
                    needRefresh = false;
                } else {
                    onceRefreshed = true;
                }
                break;

            case NEVER:
            default:
                needRefresh = false;
                break;
        }
        return needRefresh;
    }

    public void refresh() {
    }

	/* 线程 */

    private Handler handler;

    /**
     * 获取当前线程的{@link Handler}。
     * 该方法是简单单例的，一次调用之后实例就会被缓存。
     *
     * @return
     */
    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * 具体注释请参阅{@link Handler#post(Runnable)}。
     *
     * @param r
     * @return
     * @see #getHandler()
     */
    public boolean post(Runnable r) {
        return getHandler().post(r);
    }

    /**
     * 具体注释请参阅{@link Handler#postAtTime(Runnable, long)}。
     *
     * @param r
     * @param uptimeMillis
     * @return
     * @see #getHandler()
     */
    public boolean postAtTime(Runnable r, long uptimeMillis) {
        return getHandler().postAtTime(r, uptimeMillis);
    }

    /**
     * 具体注释请参阅{@link Handler#postAtTime(Runnable, Object, long)}。
     *
     * @param r
     * @param token
     * @param uptimeMillis
     * @return
     * @see #getHandler()
     */
    public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return getHandler().postAtTime(r, token, uptimeMillis);
    }

    /**
     * 具体注释请参阅{@link Handler#postDelayed(Runnable, long)}。
     *
     * @param r
     * @param delayMillis
     * @return
     * @see #getHandler()
     */
    public boolean postDelayed(Runnable r, long delayMillis) {
        return getHandler().postDelayed(r, delayMillis);
    }

    /**
     * 具体注释请参阅{@link Handler#postAtFrontOfQueue(Runnable)}。
     *
     * @param r
     * @return
     * @see #getHandler()
     */
    public boolean postAtFrontOfQueue(Runnable r) {
        return getHandler().postAtFrontOfQueue(r);
    }

	/*子Frag操作*/

    private FragHelper fragHelper;

    public FragHelper getFragHelper() {
        if (fragHelper == null) {
            fragHelper = new FragHelper(getChildFragmentManager());
        }
        return fragHelper;
    }

    /*广播*/

    private List<Object[]> fullReceivers;
    private List<Object[]> resumeReceivers;

    public void addFullReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (receiver == null || filter == null) {
            throw new IllegalArgumentException();
        }
        if (fullReceivers == null) {
            fullReceivers = new ArrayList<>();
        }
        fullReceivers.add(new Object[]{receiver, filter});
        BroadcastUtil.registerReceiver(receiver, filter);
    }

    public void addResumeReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (receiver == null || filter == null) {
            throw new IllegalArgumentException();
        }
        if (resumeReceivers == null) {
            resumeReceivers = new ArrayList<>();
        }
        resumeReceivers.add(new Object[]{receiver, filter});
    }

    /*Clearable*/

    private ClearableHolder clearableHolder;
    private boolean clearAllOnPauseEnable;

    public void setClearAllOnPauseEnable(boolean clearAllOnPauseEnable) {
        this.clearAllOnPauseEnable = clearAllOnPauseEnable;
    }

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

    /* 拓展 */

    /**
     * 获取Fragment的标题。默认返回类名。
     *
     * @return
     */
    public String getFragTitle() {
        return getClass().getSimpleName();
    }

    /**
     * 调用activity的{@link Activity#overridePendingTransition(int, int)}
     * 方法来播放activity之间的动画。
     *
     * @param enterAnim
     * @param exitAnim
     */
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        getActivity().overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * 从父容器中获取回调。
     *
     * @param listener
     * @param <T>
     * @return
     */
    protected <T> T getListenerFromParent(Class<?> listener) {
        if (!listener.isInterface()) {
            throw new IllegalArgumentException("getListenerFromParent()方法只允许获取接口");
        }
        Object obj;
        if (listener.isInstance(obj = getTargetFragment())/*targetFragment为最优先级*/
                || listener.isInstance(obj = getParentFragment()/*父fragment为次*/)
                || listener.isInstance(obj = getContext())/*Activity最次*/) {
            return (T) obj;
        }
        return null;
    }

}
