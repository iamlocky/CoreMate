package core.mate.view;

import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import core.mate.async.WeakRefLastMsgHandler;

/**
 * @author DrkCore
 * @since 2016-09-28
 */
public abstract class ScrollBinder {

    public static class Config {

        final View view;
        final Float outY;
        final Boolean topOrBottom;
        final float factor;

        public Config(View view, float outY) {
            this(view, outY, 1);
        }

        public Config(View view, float outY, float factor) {
            this.view = view;
            this.outY = outY;
            this.topOrBottom = null;
            this.factor = factor;

        }

        public Config(View view, boolean topOrBottom) {
            this(view, topOrBottom, 1);
        }

        public Config(View view, boolean topOrBottom, float factor) {
            this.view = view;
            this.outY = null;
            this.topOrBottom = topOrBottom;
            this.factor = factor;
        }
    }

    private View view;
    private float initY;
    private float outY;
    private float factor;

    public ScrollBinder(Config config) {
        this.view = config.view;
        this.factor = config.factor;

        if (factor <= 0) {
            throw new IllegalArgumentException();
        }

        view.post(() -> {//避免因为View还没初始化完成导致获取的Y为0
            initY = view.getY();

            if (config.topOrBottom != null) {
                if (config.topOrBottom) {
                    this.outY = -view.getHeight();
                } else {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    this.outY = parent.getHeight();
                    if (this.outY == 0) {//界面还没初始化
                        parent.post(() -> this.outY = parent.getHeight());
                    }
                }
            } else if (config.outY != null) {
                this.outY = config.outY;
            } else {
                throw new IllegalArgumentException();
            }
        });
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

    protected void onDelayFinishScroll(long delay) {
        if (delay <= 0) {
            onFinishScroll();
        } else {
            if (animHandler == null) {
                animHandler = new AnimHandler(this);
            }
            animHandler.sendMsgDelayed(delay);
        }
    }

    protected void onFinishScroll() {
        if (!enable) {
            return;
        }

        float y = view.getY();
        if (y == initY || y == outY) {//处于稳定状态，不作处理
            return;
        }

        if (Math.abs(y - initY) / Math.abs(outY - initY) <= 0.5F || needShow()) {//临界之内，或者需要显示
            view.animate().y(initY);
        } else {//超过临界，隐藏
            view.animate().y(outY);
        }
    }

    private float lastScrollY;
    private boolean enable = true;

    public boolean isEnable() {
        return enable;
    }

    public ScrollBinder setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }

    /**
     * 将控件回复到初始的位置。
     */
    public void show(boolean anim) {
        if (animHandler != null) {
            animHandler.clearAll();
        }
        if (view.getY() == initY) {//已经显示
            return;
        }

        if (anim) {
            view.animate().y(initY);
        } else {
            view.clearAnimation();
            view.setY(initY);
        }
    }

    /**
     * 将控件隐藏到outY的位置。
     *
     * @param anim
     */
    public void hide(boolean anim) {
        if (animHandler != null) {
            animHandler.clearAll();
        }
        if (view.getY() == outY) {//已经隐藏
            return;
        }

        if (anim) {
            view.animate().y(outY);
        } else {
            view.clearAnimation();
            view.setY(outY);
        }
    }

    protected void onScroll(float scrollY) {
        if (!enable) {
            return;
        }

        float delta = scrollY - lastScrollY;
        onScrollDelta(delta);
        lastScrollY = scrollY;
    }

    protected void onScrollDelta(float delta) {
        if (!enable) {
            return;
        }

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
