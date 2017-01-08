package core.mate.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.util.BroadcastUtil;
import core.mate.util.DataUtil;

/**
 * 封装了常用方法的Activity基类。
 *
 * @author DrkCore
 * @since 2015年8月1日12:59:01
 */
public abstract class CoreActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (needRefreshOnResume()) {
            refresh();
        }

        for (int i = 0, len = DataUtil.getSize(resumeReceivers); i < len; i++) {
            registerReceiver(resumeReceivers.get(i));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (clearAllOnPauseEnable) {
            clearAllClearable();
        }

        for (int i = 0, len = DataUtil.getSize(resumeReceivers); i < len; i++) {
            unregisterReceiver(resumeReceivers.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAllClearable();

        for (int i = 0, len = DataUtil.getSize(fullReceivers); i < len; i++) {
            unregisterReceiver(fullReceivers.get(i));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (onBackKey()) {
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_MENU:
                if (onMenuKey()) {
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

	/* 回调 */

    protected boolean onBackKey() {
        if (backKeyListeners != null && !backKeyListeners.isEmpty()) {
            for (OnActivityBackKeyListener listener : backKeyListeners) {// 安装添加顺序依次调用
                if (listener.onActivityBackKey()) {
                    return true;// 有人捕获了该返回键，终止后续的回调
                }
            }
        }
        return false;
    }

    protected boolean onMenuKey() {
        if (menuKeyListeners != null && !menuKeyListeners.isEmpty()) {
            for (OnActivityMenuKeyListener listener : menuKeyListeners) {// 安装添加顺序依次调用
                if (listener.onActivityMenuKey()) {
                    return true;// 有人捕获了该菜单键，终止后续的回调
                }
            }
        }
        return false;
    }

	/* 接口 */

    public interface OnActivityBackKeyListener {

        /**
         * 当用户按下返回键时回调该方法。
         * 如果有多个监听器则按照添加的顺序来执行，一旦有一个监听器返回了true即表示捕获了事件，
         * 之后将会终止后续的监听器的回调。
         *
         * @return
         */
        boolean onActivityBackKey();

    }

    public interface OnActivityMenuKeyListener {

        /**
         * 当用户按下菜单键时回调该方法。
         * 如果有多个监听器则按照添加的顺序来执行，一旦有一个监听器返回了true即表示捕获了事件，
         * 之后将会终止后续的监听器的回调。
         *
         * @return
         */
        boolean onActivityMenuKey();

    }

    private List<OnActivityBackKeyListener> backKeyListeners;
    private List<OnActivityMenuKeyListener> menuKeyListeners;

    public void addOnActivityBackKeyListener(OnActivityBackKeyListener listener) {
        if (backKeyListeners == null) {// 创建list单例
            backKeyListeners = new ArrayList<>(1);// 默认大小1就够了
        }
        backKeyListeners.add(listener);
    }

    public void addOnActivityMenuKeyListener(OnActivityMenuKeyListener listener) {
        if (menuKeyListeners == null) {
            menuKeyListeners = new ArrayList<>(1);
        }
        menuKeyListeners.add(listener);
    }

    public void removeOnActivityBackKeyListener(OnActivityBackKeyListener listener) {
        if (backKeyListeners != null) {
            backKeyListeners.remove(listener);
        }
    }

    public void removeOnActivityMenuKeyListener(OnActivityMenuKeyListener listener) {
        if (menuKeyListeners != null) {
            menuKeyListeners.remove(listener);
        }
    }

	/* 界面刷新 */

    private RefreshRate refreshRate = RefreshRate.ONCE;
    private boolean onceRefreshed;

    /**
     * 获取当前的刷新频率，默认返回{@link RefreshRate#ONCE}，即只刷新一次。
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

	/* Toolbar控制 */

    private int toolbarId;
    private Toolbar toolbar;

    public Toolbar getToolbar() {
        // 获取toolbar
        if (toolbar == null && toolbarId != 0) {
            toolbar = (Toolbar) findViewById(toolbarId);
        }
        return toolbar;
    }

    /**
     * 指定用于从界面获取toolbar的id。请在{@link #setContentView(int)}之前调用该方法。
     *
     * @param toolbarId
     */
    public void configToolbarId(@IdRes int toolbarId) {
        this.toolbarId = toolbarId;
    }

    /**
     * 设置toolbar导航为返回按钮，并设置点击事件。
     * 该方法会调用{@link #setSupportActionBar(Toolbar)}
     *
     * @param toolbar
     * @param iconId          导航的图标，为0则不设置图标
     * @param onClickListener
     */
    public void configToolbarNav(Toolbar toolbar, int iconId, OnClickListener onClickListener) {
        if (iconId > 0) {
            toolbar.setNavigationIcon(iconId);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    /**
     * 具体实现请参阅{@link #configToolbarNavToFinish(Toolbar, int)}
     */
    public void configToolbarNavToFinish(Toolbar toolbar) {
        configToolbarNavToFinish(toolbar, 0);
    }

    /**
     * 设置toolbar导航为返回按钮，并设置点击后关闭activity。
     * 具体实现请参阅{@link #configToolbarNav(Toolbar, int, OnClickListener)}
     *
     * @param toolbar 导航的按钮
     * @param iconId  导航的图标
     */
    public void configToolbarNavToFinish(Toolbar toolbar, int iconId) {
        configToolbarNav(toolbar, iconId, new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

	/* 视图层次 */

    /**
     * 添加视图到Activity根布局上。由于根布局是{@link android.widget.FrameLayout#}因而新添加的视图会悬浮在原有界面上，
     * 默认宽高均为{@link android.view.ViewGroup.LayoutParams#MATCH_PARENT}。
     * 具体实现请参阅{@link #addContentView(View, ViewGroup.LayoutParams)}
     *
     * @param view
     */
    public void addContentView(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.addContentView(view, params);
    }

    /**
     * 从Activity的根布局中移除控件
     *
     * @param view
     */
    public void removeContentView(View view) {
        getContentViewContainer().removeView(view);
    }

    /**
     * 获取通过setContentView(int)等方法设置的内容视图。
     *
     * @return
     */
    public View getContentView() {
        return getContentViewContainer().getChildAt(0);
    }

    /**
     * 获取位于DecorView之下的内容视图的容器，即{@link #setContentView(int)}所填充到的父布局。
     *
     * @return
     */
    public ViewGroup getContentViewContainer() {
        return (ViewGroup) getDecorViewContainer().findViewById(Window.ID_ANDROID_CONTENT);
    }

    /**
     * 通过getWindow().getDecorView()方法获取到的视图。该视图是当前activity对应的window的最顶层的视图，
     * 即在其之上再不存在任何视图了。其大小就是屏幕的大小。需要注意的是，状态栏是浮动在该View之上的。
     *
     * @return
     */
    public ViewGroup getDecorViewContainer() {
        return (ViewGroup) getWindow().getDecorView();
    }

	/*Fragment操作*/

    private FragHelper fragHelper;

    public FragHelper getFragHelper() {
        if (fragHelper == null) {
            fragHelper = new FragHelper(getSupportFragmentManager());
        }
        return fragHelper;
    }

	/* 线程 */

    private Handler handler;

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    public boolean post(Runnable r) {
        return getHandler().post(r);
    }

    public boolean postAtTime(Runnable r, long uptimeMillis) {
        return getHandler().postAtTime(r, uptimeMillis);
    }

    public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        return getHandler().postAtTime(r, token, uptimeMillis);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return getHandler().postDelayed(r, delayMillis);
    }

    public boolean postAtFrontOfQueue(Runnable r) {
        return getHandler().postAtFrontOfQueue(r);
    }

    /*广播*/

    private List<CoreReceiver> fullReceivers;
    private List<CoreReceiver> resumeReceivers;

    /**
     * 注册长时间监听的广播。该广播会在调用该方法时自动注册，并在{@link #onDestroy()}中注销。
     *
     * @param receiver
     */
    public void addFullReceiver(CoreReceiver receiver) {
        if (fullReceivers == null) {
            fullReceivers = new ArrayList<>();
        }
        fullReceivers.add(receiver);
        registerReceiver(receiver);
    }

    /**
     * 注册只在{@link #onResume()}和{@link #onPause()}之间的窗口启用的广播。
     * <p>
     * 注意，如果在窗口期内调用该方法的话只会在下个窗口期开始时真正注册到上下文中，
     * 因而只建议在{@link #onCreate(Bundle)}中使用该方法。
     *
     * @param receiver
     */
    public void addResumeReceiver(CoreReceiver receiver) {
        if (resumeReceivers == null) {
            resumeReceivers = new ArrayList<>();
        }

        resumeReceivers.add(receiver);
    }

    public void registerReceiver(CoreReceiver receiver) {
        BroadcastUtil.getManager(receiver.isLocal()).register(receiver, receiver.getFilter());
    }

    public void unregisterReceiver(CoreReceiver receiver) {
        BroadcastUtil.getManager(receiver.isLocal()).unregister(receiver);
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

}
