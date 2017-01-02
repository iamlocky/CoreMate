package core.mate.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import core.mate.R;
import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.util.BroadcastUtil;
import core.mate.util.ClassUtil;
import core.mate.util.ContextUtil;
import core.mate.util.LogUtil;
import core.mate.util.ViewUtil;

/**
 * 简单封装的DialogFrag基类
 *
 * @author DrkCore
 * @since 2015年10月17日12:48:35
 */
public abstract class CoreDlgFrag extends DialogFragment implements DialogInterface.OnKeyListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dismissIfRecreated && savedInstanceState != null) {
            dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dlg = getDialog();
        Window dlgWin = dlg.getWindow();
        onPrepareDialogWindow(savedInstanceState, dlg, dlgWin);

        if (onDlgListener != null) {
            onDlgListener.onShow(this);
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDlgListener != null) {
            onDlgListener.onDismiss(this);
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Throwable e) {
            LogUtil.e(e);
        }
    }

    /* 配置 */

    private Integer winAnimStyle;
    private Float dimAmount;
    private Drawable winBgDrawable;

    public CoreDlgFrag setWinAnimStyle(@Nullable Integer winAnimStyle) {
        this.winAnimStyle = winAnimStyle;
        return this;
    }

    public CoreDlgFrag setDimAmount(@Nullable Float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    public CoreDlgFrag setWinBgDrawable(@Nullable Drawable winBgDrawable) {
        this.winBgDrawable = winBgDrawable;
        return this;
    }

    public CoreDlgFrag setWinBgColor(@Nullable @ColorInt Integer color) {
        if (color != null) {
            setWinBgDrawable(new ColorDrawable(color));
        } else {
            setWinBgDrawable(null);
        }
        return this;
    }

    public CoreDlgFrag setWinBgResource(@Nullable @DrawableRes Integer winBgResource) {
        if (winBgResource != null) {
            setWinBgDrawable(ContextUtil.getDrawable(winBgResource));
        } else {
            setWinBgDrawable(null);
        }
        return this;
    }

    public CoreDlgFrag setPanelStyle() {
        setStyle(STYLE_NO_FRAME, 0);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setWinAnimStyle(R.style.CoreWindowAnimSlideTopStyle);
        setGravity(Gravity.BOTTOM);

        return this;
    }

    private Integer gravity;
    private Integer width;
    private Integer height;

    public CoreDlgFrag setGravity(@Nullable Integer gravity) {
        this.gravity = gravity;
        return this;
    }

    public CoreDlgFrag setWidth(@Nullable Integer width) {
        this.width = width;
        return this;
    }

    public CoreDlgFrag setHeight(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    public CoreDlgFrag setSize(@Nullable Integer width, @Nullable Integer height) {
        this.width = width;
        this.height = height;
        return this;
    }

    protected CoreDlgFrag setWidthPercent(@Nullable Float percent) {
        if (percent != null) {
            if (percent < 0 || percent > 1) {
                throw new IllegalArgumentException("width percent " + percent + " 不合法");
            }
            setWidth((int) (ViewUtil.getScreenWidth() * percent));
        } else {
            setWidth(null);
        }
        return this;
    }

    protected CoreDlgFrag setHeightPercent(@Nullable Float percent) {
        if (percent != null) {
            if (percent < 0 || percent > 1) {
                throw new IllegalArgumentException("height percent " + percent + " 不合法");
            }
            setHeight((int) (ViewUtil.getScreenHeight() * percent));
        } else {
            setHeight(null);
        }
        return this;
    }

    private Integer x;
    private Integer y;

    public CoreDlgFrag setX(@Nullable Integer x) {
        this.x = x;
        return this;
    }

    public CoreDlgFrag setY(@Nullable Integer y) {
        this.y = y;
        return this;
    }

    public CoreDlgFrag setPosition(@Nullable Integer x, @Nullable Integer y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * 配置对话框的window，将在{@link #onActivityCreated(Bundle)}中回调。
     *
     * @param savedInstanceState
     * @param dlg
     * @param dlgWin
     */
    protected void onPrepareDialogWindow(@Nullable Bundle savedInstanceState, Dialog dlg, Window dlgWin) {
        setCancelOnTouchOutSideEnable(cancelOnTouchOutSideEnable);
        dlg.setOnKeyListener(this);

        if (winAnimStyle != null) {
            dlgWin.setWindowAnimations(winAnimStyle);
        }

        if (winBgDrawable != null) {
            dlgWin.setBackgroundDrawable(winBgDrawable);
        }
        WindowManager.LayoutParams params = dlgWin.getAttributes();
        if (gravity != null) {
            params.gravity = gravity;
        }
        if (width != null) {
            params.width = width;
        }
        if (height != null) {
            params.height = height;
        }
        if (x != null) {
            params.x = x;
        }
        if (y != null) {
            params.y = y;
        }
        if (dimAmount != null) {
            params.flags |=WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dlgWin.setDimAmount(dimAmount);
        }
        dlgWin.setAttributes(params);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return false;
    }

	/* 刷新 */

    private RefreshRate refreshRate;
    private boolean onceRefreshed;

    public RefreshRate getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(RefreshRate refreshRate) {
        this.refreshRate = refreshRate;
    }

    private boolean needRefreshOnResume() {
        refreshRate = refreshRate != null ? refreshRate : RefreshRate.ONCE;
        switch (refreshRate) {
            case ALWAYS:
                return true;

            case ONCE:
                if (onceRefreshed) {
                    return false;
                } else {
                    onceRefreshed = true;
                    return true;
                }

            case NEVER:
            default:
                return false;
        }
    }

    public void refresh() {
    }

	/* 显示 */

    public interface OnDlgListener {

        void onShow(CoreDlgFrag dlgFrag);

        void onDismiss(CoreDlgFrag dlgFrag);

    }

    private OnDlgListener onDlgListener;

    public CoreDlgFrag setOnDlgListener(OnDlgListener onDlgListener) {
        if (onDlgListener == this) {// 自己给自己设置监听？这并不好吧……
            throw new IllegalArgumentException("不允许将自己设为自己的监听器，这可能导致无限递归！如果你需要该方法，请直接重写。");
        }
        this.onDlgListener = onDlgListener;
        return this;
    }

    /**
     * 显示对话框，使用类名作为tag<br>
     *
     * @param context
     */
    public void show(FragmentActivity context) {
        show(context.getSupportFragmentManager(), ClassUtil.getTypeName(this));
    }

    /**
     * 显示对话框，使用类名作为tag
     *
     * @param fragMgr
     */
    public void show(FragmentManager fragMgr) {
        show(fragMgr, ClassUtil.getTypeName(this));
    }

    /**
     * 显示对话框，使用类名作为tag
     *
     * @param frag
     */
    public void show(Fragment frag) {
        show(frag.getChildFragmentManager());
    }

    private boolean dismissIfRecreated = true;
    private boolean cancelOnTouchOutSideEnable = true;

    public boolean isDismissIfRecreated() {
        return dismissIfRecreated;
    }

    public CoreDlgFrag setDismissIfRecreated(boolean dismissIfRecreated) {
        this.dismissIfRecreated = dismissIfRecreated;
        return this;
    }

    /**
     * 是否启用点击外部区域关闭对话框。默认为true。
     *
     * @return
     */
    public boolean isCancelOnTouchOutSideEnable() {
        return cancelOnTouchOutSideEnable;
    }

    /**
     * 设置开闭点击外部区域关闭对话框的功能，默认情况下会启用。
     *
     * @param cancelOnTouchOutSideEnable
     * @return
     */
    public CoreDlgFrag setCancelOnTouchOutSideEnable(boolean cancelOnTouchOutSideEnable) {
        this.cancelOnTouchOutSideEnable = cancelOnTouchOutSideEnable;
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(cancelOnTouchOutSideEnable);
        }
        return this;
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

    /*Fragment操作*/

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
     * 从父容器中获取回调。
     *
     * @param listener
     * @param <T>
     * @return
     */
    public <T> T getListenerFromParent(Class<?> listener) {
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
}
