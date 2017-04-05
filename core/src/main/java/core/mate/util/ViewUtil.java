package core.mate.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

import core.mate.Core;

/**
 * 封装了对控件的一些操作的工具类
 *
 * @author DrkCore
 * @since 2015年11月13日18:47:08
 */
public final class ViewUtil {

    private ViewUtil() {
    }

	/*视图参数*/

    private static final float density;
    private static final float scaledDensity;
    private static final int widthPixels;
    private static final int heightPixels;

    private static final int statusBarHeight;
    private static final int navBtnHeight;

    static {
        // 屏幕属性
        DisplayMetrics displayMetrics = ContextUtil.getResources().getDisplayMetrics();
        density = displayMetrics.density;
        scaledDensity = displayMetrics.scaledDensity;

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager winMgr = (WindowManager) Core.getInstance()
                    .getAppContext().getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            winMgr.getDefaultDisplay().getRealSize(size);
            width = size.x;
            height = size.y;
        }

        widthPixels = width;
        heightPixels = height;

        //状态栏
        Resources res = ContextUtil.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId == 0) {
            try {//尝试反射获取
                Class<?> clz = Class.forName("com.android.internal.R$dimen");
                Object obj = clz.newInstance();
                Field field = clz.getField("status_bar_height");
                resourceId = Integer.parseInt(field.get(obj).toString());
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = 0;
        }

        //虚拟按键
        navBtnHeight = heightPixels - displayMetrics.heightPixels;
    }

    public static float getDensity() {
        return density;
    }

    public static float getScaledDensity() {
        return scaledDensity;
    }

    public static int getScreenWidth() {
        return widthPixels;
    }

    /**
     * 获取设备实际屏幕高度。包含虚拟按键，如果存在的话。
     *
     * @return
     */
    public static int getScreenHeight() {
        return heightPixels;
    }

    public static int getSmallestScreenLength() {
        return Math.min(getScreenWidth(), getScreenHeight());
    }

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    /**
     * 获取虚拟导航按钮高度。为0表示不存在虚拟导航按键。
     *
     * @return
     */
    public static int getNavBtnHeight() {
        return navBtnHeight;
    }

    public static boolean hasNavBtnHeight() {
        return navBtnHeight > 0;
    }

    /* 单位转换 */

    public static int dpToPx(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5F);
    }

    public static int pxToDp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5F);
    }

    public static int pxToSp(float pxValue) {
        return (int) (pxValue / getScaledDensity() + 0.5f);
    }

    public static int spToPx(float spValue) {
        return (int) (spValue * getScaledDensity() + 0.5f);
    }

	/* 动画/可见性 */

    /**
     * 切换布局的可见性。当布局的可见性为{@link View#VISIBLE}时切换到{@link View#GONE}，
     * 当可见性为{@link View#GONE}或者{@link View#INVISIBLE}时，切换到{@link View#VISIBLE}
     *
     * @param view
     */
    public static void toggleViewVisible(View view) {
        int nextVisibility = view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        view.setVisibility(nextVisibility);
    }


    public static Animation showWithAnim(View view, int animRes) {
        return showWithAnim(view, animRes, null);
    }

    /**
     * 显示控件后播放动画。如果animRes小于或者等于0则表示不使用动画。
     * 如果view已经处于{@link View#VISIBLE}状态则不做任何处理。
     *
     * @param view
     * @param animRes
     * @param listener
     * @return
     */
    public static Animation showWithAnim(View view, int animRes, @Nullable Animation.AnimationListener listener) {
        Animation anim = null;
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            if (animRes > 0) {
                anim = AnimationUtils.loadAnimation(Core.getInstance().getAppContext(), animRes);
                anim.setAnimationListener(listener);
                view.startAnimation(anim);
            }
        }
        return anim;
    }

    @Nullable
    public static Animation hideWithAnim(View view, int animRes) {
        return hideWithAnim(view, animRes, null);
    }

    /**
     * 播放动画后隐藏控件。如果animRes小于或者等于0则表示不使用动画。
     * 如果view已经处于{@link View#GONE}状态则不做任何处理。
     *
     * @param view
     * @param animRes
     * @param listener
     */
    @Nullable
    public static Animation hideWithAnim(View view, int animRes, @Nullable Animation.AnimationListener listener) {
        Animation anim = null;
        if (view.getVisibility() != View.GONE) {
            if (animRes > 0) {
                anim = AnimationUtils.loadAnimation(Core.getInstance().getAppContext(), animRes);
                anim.setAnimationListener(listener);
                view.startAnimation(anim);
            }
            view.setVisibility(View.GONE);
        }
        return anim;
    }

	/* 控件尺寸 */

    public static int measureViewHeight(View v) {
        // 获取高度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    public static int measureViewWidth(View v) {
        // 获取高度
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    /**
     * 动态调整ListView的高度。当你将ListView嵌入ScrollView的时候可能需要用到该方法。
     *
     * @param listView
     */
    public static void adjustListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        setHeight(listView, height);
    }

    public static void setVerticalMargin(View view, int margin) {
        setVerticalMargin(view, margin, margin);
    }

    public static void setVerticalMargin(View view, @Nullable Integer top, @Nullable Integer bottom) {
        setMargin(view, null, top, null, bottom);
    }

    public static void setHorizontalMargin(View view, int margin) {
        setHorizontalMargin(view, margin, margin);
    }

    public static void setHorizontalMargin(View view, @Nullable Integer left, @Nullable Integer right) {
        setMargin(view, left, null, right, null);
    }

    public static void setMargin(View view, int margin) {
        setMargin(view, margin, margin, margin, margin);
    }

    /**
     * 设置margin。其中left、top、right、bottom如果为null，则表示不处理。
     *
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setMargin(View view, @Nullable Integer left, @Nullable Integer top, @Nullable Integer right, @Nullable Integer bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (params == null) {
            throw new IllegalStateException("无法获取控件的LayoutParams");
        }
        if (left != null) {
            params.leftMargin = left;
        }
        if (top != null) {
            params.topMargin = top;
        }
        if (right != null) {
            params.rightMargin = right;
        }
        if (bottom != null) {
            params.bottomMargin = bottom;
        }
        view.setLayoutParams(params);
    }

    public static void setHeight(View view, int height) {
        setSize(view, null, height);
    }

    public static void setWidth(View view, int width) {
        setSize(view, width, null);
    }

    public static void setSize(View view, @Nullable Integer width, @Nullable Integer height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            throw new IllegalStateException("无法获取控件的LayoutParams");
        }

        if (width != null) {
            params.width = width;
        }
        if (height != null) {
            params.height = height;
        }
        view.setLayoutParams(params);
    }

	/* View截图 */

    /**
     * 获取视图的截图。
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapByDrawingCache(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //因为原图可能会被回收，这里创建一个拷贝
        Bitmap img = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return img;
    }

    public static Bitmap getBitmapByDraw(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        view.setWillNotDraw(true);
        view.draw(canvas);
        view.setWillNotDraw(false);
        return bmp;
    }

}
