package core.mate.view;

import android.os.Message;
import android.support.annotation.NonNull;

import core.mate.async.WeakRefLastMsgHandler;

/**
 * @author DrkCore
 * @since 2016/11/18
 */
public class StableIndicatorWrapper implements ITaskIndicator {

    private static class RefreshHandler extends WeakRefLastMsgHandler<StableIndicatorWrapper> {

        public RefreshHandler(StableIndicatorWrapper ref) {
            super(ref);
        }

        @Override
        protected void onLastMessageLively(@NonNull StableIndicatorWrapper ref, Message msg) {
            ref.refreshState();
        }
    }

    public static final long DEFAULT_DELAY = 500;

    private final RefreshHandler handler = new RefreshHandler(this);
    private final ITaskIndicator indicator;
    private final long delay;

    public StableIndicatorWrapper(ITaskIndicator indicator) {
        this(indicator, DEFAULT_DELAY);
    }

    public StableIndicatorWrapper(ITaskIndicator indicator, long delay) {
        this.indicator = indicator;
        this.delay = delay;
    }

    @Override
    public boolean isProgressing() {
        return progressing;
    }

    private boolean progressing;

    @Override
    public void showProgress() {
        progressing = true;
        handler.sendMsgDelayed(delay);
    }
    
    @Override
    public void hideProgress() {
        progressing = false;
        handler.sendMsgDelayed(delay);
    }

    private void refreshState() {
        if (progressing) {
            indicator.showProgress();
        } else {
            indicator.hideProgress();
        }
    }
}
