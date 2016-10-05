package core.mate.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;

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

	private static float density;
	private static float scaledDensity;
	private static int widthPixels;
	private static int heightPixels;

	static {// 初始化
		DisplayMetrics displayMetrics = ResUtil.getResources().getDisplayMetrics();
		density = displayMetrics.density;
		scaledDensity = displayMetrics.scaledDensity;
		widthPixels = displayMetrics.widthPixels;
		heightPixels = displayMetrics.heightPixels;
	}

	public static float getDisplayMetricsDensity() {
		return density;
	}

	public static float getDisplayMetricsScaledDensity() {
		return scaledDensity;
	}

	public static int getScreenWidthPx() {
		return widthPixels;
	}

	public static int getScreenHeightPx() {
		return heightPixels;
	}


	/* 单位转换 */

	public static int dpToPx(float dpValue) {
		return (int) (dpValue * getDisplayMetricsDensity() + 0.5F);
	}

	public static int pxToDp(float pxValue) {
		return (int) (pxValue / getDisplayMetricsDensity() + 0.5F);
	}

	public static int pxToSp(float pxValue) {
		return (int) (pxValue / getDisplayMetricsScaledDensity() + 0.5f);
	}

	public static int spToPx(float spValue) {
		return (int) (spValue * getDisplayMetricsScaledDensity() + 0.5f);
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

	public static int getViewHeight(View v) {
		// 获取高度
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		return v.getMeasuredHeight();
	}

	public static int getViewWidth(View v) {
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

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
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
