package core.mate.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import core.mate.util.ContextUtil;
import core.mate.util.ViewUtil;

/**
 * 分割线
 *
 * @author DrkCore
 * @since 2016年2月1日00:16:53
 */
public final class Divider {

    public static final Drawable DEFAULT_DRAWABLE = new ColorDrawable(Color.GRAY);

    private int heightPx;
    private Drawable drawable = DEFAULT_DRAWABLE;

    public int getHeightPx() {
        if (heightPx <= 0) {
            heightPx = 1;//默认一个像素
        }
        return heightPx;
    }

    public Divider setHeightPx(int heightPx) {
        this.heightPx = heightPx;
        return this;
    }

    public Divider setHeightDp(float heightDp) {
        heightPx = ViewUtil.dpToPx(heightDp);
        return this;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public Divider setDrawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public Divider setDrawableColor(@ColorInt int color) {
        return setDrawable(new ColorDrawable(color));
    }

    public Divider setDrawableRes(@DrawableRes int bgResId) {
        this.drawable = ContextUtil.getDrawable(bgResId);
        return this;
    }

    public Divider() {
    }

    public Divider(int heightPx) {
        this.heightPx = heightPx;
    }

    public Divider(int heightPx, Drawable drawable) {
        setHeightPx(heightPx);
        setDrawable(drawable);
    }

    public Divider(int heightPx, int bgResId) {
        setHeightPx(heightPx);
        setDrawableRes(bgResId);
    }
}
