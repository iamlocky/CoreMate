package core.mate.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import core.mate.util.ViewUtil;

/**
 * @author DrkCore
 * @since 2016年1月18日18:24:09
 */
public class SimpleViewHolder<Item> extends AbsViewHolder<Item> {

    public SimpleViewHolder(View v) {
        super(v);
    }

	/*获取控件*/

    private SparseArray<View> viewArray;

    public <V extends View> V getViewById(@IdRes int id) {
        if (viewArray == null) {
            viewArray = new SparseArray<>();
        }

        View view = viewArray.get(id);
        if (view == null) {
            view = getView().findViewById(id);
            if (view != null) {
                viewArray.put(id, view);
            }
        }
        return (V) view;
    }

    /*Holder处理*/

    public void setHolderWidth(int width) {
        setHolderSize(width, null);
    }

    public void setHolderHeight(int height) {
        setHolderSize(null, height);
    }

    public void setHolderSize(@Nullable Integer width, @Nullable Integer height) {
        ViewUtil.setSize(getView(), width, height);
    }

    public void setHolderBackgroundColor(@ColorInt int color) {
        View view = getView();
        view.setBackgroundColor(color);
    }

    public void setHolderBackgroundResource(@DrawableRes int drawableRes) {
        View view = getView();
        view.setBackgroundResource(drawableRes);
    }

    public void setHolderBackgroundDrawable(Drawable drawable) {
        View view = getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

	/*子控件处理*/

    public void setBackgroundColor(@IdRes int id, @ColorInt int color) {
        View view = getViewById(id);
        view.setBackgroundColor(color);
    }

    public void setBackgroundResource(@IdRes int id, @DrawableRes int drawableRes) {
        View view = getViewById(id);
        view.setBackgroundResource(drawableRes);
    }

    public void setBackgroundDrawable(@IdRes int id, Drawable drawable) {
        View view = getViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public void setImageDrawable(@IdRes int id, Drawable drawable) {
        ImageView imageView = getViewById(id);
        imageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(@IdRes int id, Bitmap bmp) {
        ImageView imageView = getViewById(id);
        imageView.setImageBitmap(bmp);
    }

    public void setImageResource(@IdRes int id, @DrawableRes int drawableRes) {
        ImageView imageView = getViewById(id);
        imageView.setImageResource(drawableRes);
    }

    public void setText(@IdRes int id, @StringRes int stringRes) {
        TextView textView = getViewById(id);
        textView.setText(stringRes);
    }

    public void setText(@IdRes int id, CharSequence string) {
        TextView textView = getViewById(id);
        textView.setText(string);
    }

    /**
     * 设置id指定控件的点击事件，具体实现请参阅{@link #setOnClickListener(int, View.OnClickListener, boolean)}，
     * 其中autoTag默认为true
     *
     * @param id
     * @param listener
     */
    public void setOnClickListener(@IdRes int id, View.OnClickListener listener) {
        setOnClickListener(id, listener, true);
    }

    /**
     * 设置id指定控件的点击事件。当autoTag为true时将会将本Holder设置为控件的tag
     *
     * @param id
     * @param listener
     * @param autoTag
     */
    public void setOnClickListener(@IdRes int id, View.OnClickListener listener, boolean autoTag) {
        View view = getViewById(id);
        view.setOnClickListener(listener);
        if (autoTag) {
            view.setTag(this);
        }
    }

    public void setVisibility(@IdRes int id, int visibility) {
        View view = getViewById(id);
        view.setVisibility(visibility);
    }

    public void setVisible(@IdRes int id, boolean visible) {
        View view = getViewById(id);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setChecked(@IdRes int id, boolean checked) {
        Checkable checkable = getViewById(id);
        checkable.setChecked(checked);
    }

    public boolean isChecked(@IdRes int id) {
        Checkable checkable = getViewById(id);
        return checkable.isChecked();
    }

    public void setHeight(@IdRes int id, int height) {
        setSize(id, null, height);
    }

    public void setWidth(@IdRes int id, int width) {
        setSize(id, width, null);
    }

    public void setSize(@IdRes int id, @Nullable Integer width, @Nullable Integer height) {
        View view = getViewById(id);
        ViewUtil.setSize(view, width, height);
    }
}
