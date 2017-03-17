package core.mate.app;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import core.mate.async.Clearable;
import core.mate.async.ClearableHolder;
import core.mate.async.ClearableWrapper;
import core.mate.async.CoreHandler;

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

        if (receiverHelper != null) {
            receiverHelper.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (clearAllOnPauseEnable) {
            clearAllClearable();
        }

        if (receiverHelper != null) {
            receiverHelper.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAllClearable();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (receiverHelper != null) {
            receiverHelper.onDestroy();
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

	/* 视图 */

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

    public static final int FULL_SCREEN_UI_OPTION;

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            FULL_SCREEN_UI_OPTION = View.GONE;
        } else {
            FULL_SCREEN_UI_OPTION = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
    }

    public boolean isFullScreen() {
        int flag = getWindow().getAttributes().flags;
        return (flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    public void toggleFullScreen() {
        setFullScreen(!isFullScreen());
    }

    public void setFullScreen(boolean fullScreen) {
        if (isFullScreen() != fullScreen) {
            View decorView = this.getWindow().getDecorView();
            if (fullScreen) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                decorView.setSystemUiVisibility(FULL_SCREEN_UI_OPTION);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                int uiOption = View.VISIBLE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiOption &= ~FULL_SCREEN_UI_OPTION;
                }
                decorView.setSystemUiVisibility(uiOption);
            }
        }
    }

	/*辅助类*/

    private FragHelper fragHelper;

    public FragHelper getFragHelper() {
        if (fragHelper == null) {
            fragHelper = new FragHelper(getSupportFragmentManager());
        }
        return fragHelper;
    }

    private ReceiverHelper receiverHelper;

    public ReceiverHelper getReceiverHelper() {
        if (receiverHelper == null) {
            receiverHelper = new ReceiverHelper();
        }
        return receiverHelper;
    }

    private Handler handler;

    public Handler getHandler() {
        if (handler == null) {
            CoreHandler coreHandler = new CoreHandler();
            addClearable(coreHandler);
            handler = coreHandler;

        }
        return handler;
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
