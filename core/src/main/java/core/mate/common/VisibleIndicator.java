package core.mate.common;

import android.view.View;

public class VisibleIndicator implements ITaskIndicator {

    private final View view;
    private boolean hideByGoneEnable = true;

    /**
     * 设为true时当调用{@link #hideProgress()}时使用{@link View#GONE}来替代原本的{@link View#INVISIBLE}
     *
     * @param hideByGoneEnable
     * @return
     */
    public VisibleIndicator setHideByGoneEnable(boolean hideByGoneEnable) {
        this.hideByGoneEnable = hideByGoneEnable;
        return this;
    }

    public VisibleIndicator(View view) {
        this.view = view;
    }

    @Override
    public boolean isProgressing() {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showProgress() {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        view.setVisibility(hideByGoneEnable ? View.GONE : View.INVISIBLE);
    }
}
