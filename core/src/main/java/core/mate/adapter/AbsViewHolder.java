package core.mate.adapter;

import android.view.View;

/**
 * @author DrkCore
 * @since 2016-10-15
 */
abstract class AbsViewHolder {

    private int position;
    private final View view;

    /**
     * 获取Holder携带的View。
     *
     * @return
     */
    public final View getView() {
        return view;
    }

    /**
     * 获取Holder携带的View，并转化成指定类型。
     *
     * @param <T>
     * @return
     */
    public final <T extends View> T getCastView() {
        return (T) getView();
    }

    public final int getPosition() {
        return position;
    }

    public final void setPosition(int position) {
        this.position = position;
    }

    public AbsViewHolder(View v) {
        this.view = v;
    }

}
