package core.mate.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import core.mate.util.ContextUtil;

/**
 * 分割线
 *
 * @author DrkCore
 * @since 2016年2月1日00:16:53
 */
public final class Divider {

    public static final Drawable DEFAULT_DRAWABLE = new ColorDrawable(Color.GRAY);

    private int height;
    private Drawable drawable = DEFAULT_DRAWABLE;

    public int getHeight() {
        if (height <= 0) {
            height = 1;//默认一个像素
        }
        return height;
    }

    public Divider setHeight(int heightPx) {
        this.height = heightPx;
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

    public Divider(int height) {
        this.height = height;
    }

    public Divider(int height, Drawable drawable) {
        setHeight(height);
        setDrawable(drawable);
    }

    public Divider(int height, int bgResId) {
        setHeight(height);
        setDrawableRes(bgResId);
    }
}
