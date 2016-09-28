package core.mate.view;

import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import core.mate.async.WeakRefLastMsgHandler;
import core.mate.util.ViewUtil;

/**
 * @author DrkCore
 * @since 2016-09-28
 */
public abstract class ScrollBinder {

	private View view;
	private float initY;
	private float outY;
	private float factor;

	public ScrollBinder(View view) {
		this(view, 1);
	}

	public ScrollBinder(View view, float factor) {
		this(view, -ViewUtil.getViewHeight(view), factor);
	}

	public ScrollBinder(View view, float outY, float factor) {
		this.view = view;
		this.initY = view.getY();
		this.outY = outY;
		this.factor = factor;

		if (outY == initY || factor <= 0) {
			throw new IllegalArgumentException();
		}
	}

	/*回调*/

	private static class AnimHandler extends WeakRefLastMsgHandler<ScrollBinder> {

		public AnimHandler(ScrollBinder ref) {
			super(ref);
		}

		@Override
		protected void onLastMessageLively(@NonNull ScrollBinder ref, Message msg) {
			ref.onFinishScroll();
		}
	}

	private AnimHandler animHandler;

	protected final void onDelayFinishScroll(long delay) {
		if (delay <= 0) {
			onFinishScroll();
		} else {
			if (animHandler == null) {
				animHandler = new AnimHandler(this);
			}
			animHandler.sendMsgDelayed(delay);
		}
	}

	protected final void onFinishScroll() {
		float y = view.getY();
		if (y == initY || y == outY) {//处于稳定状态，不作处理
			return;
		}

		if (Math.abs(y - initY) / Math.abs(outY - initY) <= 0.5F || needShow()) {//临界之内，或者需要隐藏
			view.animate().y(initY);
		} else {//超过临界，隐藏
			view.animate().y(outY);
		}
	}

	private float lastScrollY;

	protected final void onScroll(float scrollY) {
		float delta = scrollY - lastScrollY;
		onScrollDelta(delta);
		lastScrollY = scrollY;
	}

	protected final void onScrollDelta(float delta) {
		delta *= factor * (outY < initY ? -1 : 1);
		//限制滚动范围
		float curY = view.getY();
		float toY = getLimitedY(curY + delta);
		if (curY != toY) {
			view.setY(toY);
		}
	}

	private float getLimitedY(float toY) {
		if (outY < initY) {//随滚动布局一同滚动
			if (toY < outY) {
				toY = outY;
			} else if (toY > initY) {
				toY = initY;
			}
		} else {//反向滚动
			if (toY < initY) {
				toY = initY;
			} else if (toY > outY) {
				toY = outY;
			}
		}
		return toY;
	}

	protected boolean needShow() {
		return false;
	}
}
