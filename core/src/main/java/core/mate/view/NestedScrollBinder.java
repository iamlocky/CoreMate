package core.mate.view;

import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author DrkCore
 * @since 2016-10-27
 */
public class NestedScrollBinder extends ScrollBinder implements NestedScrollView.OnScrollChangeListener, View.OnTouchListener {


    private NestedScrollView scrollView;

    public NestedScrollBinder(Config config) {
        this(config, null);
    }

    public NestedScrollBinder(Config config, NestedScrollView scrollView) {
        super(config);
        if (scrollView != null) {
            bind(scrollView);
        }
    }

    public void bind(NestedScrollView scrollView) {
        this.scrollView = scrollView;
        scrollView.setOnScrollChangeListener(this);
        scrollView.setOnTouchListener(this);
    }

    @Override
    protected boolean needShow() {
        if (scrollView != null) {
            return scrollView.getScrollY() == 0;
        }
        return super.needShow();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        onScroll(scrollY);
    }

    public static final long DEFAULT_FINISH_DELAY = 256L;

    private long touchUpFinishDelay = DEFAULT_FINISH_DELAY;

    public NestedScrollBinder disableTouchUpFinish() {
        this.touchUpFinishDelay = 0;
        return this;
    }

    /**
     * 设置touch事件up后用于调用{@link #onDelayFinishScroll(long)}的延迟。
     * <p>
     * 当其小于等于零表示不启用该事件。
     *
     * @param touchUpFinishDelay
     * @return
     */
    public NestedScrollBinder setTouchUpFinish(long touchUpFinishDelay) {
        this.touchUpFinishDelay = touchUpFinishDelay;
        return this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (touchUpFinishDelay > 0) {
                    onDelayFinishScroll(touchUpFinishDelay);
                }
                break;
        }
        return false;
    }
}
