package core.mate.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import core.mate.util.ViewUtil;

/**
 * @author DrkCore
 * @since 2016年2月24日22:32:35
 */
public class SimpleRecyclerViewHolder extends RecyclerView.ViewHolder {

    public SimpleRecyclerViewHolder(View v) {
        super(v);
    }

	/*获取控件*/

    private SparseArray<View> viewArray;

    public final <T extends View> T getCastView() {
        return (T) itemView;
    }

    public final <V extends View> V getViewById(@IdRes int id) {
        if (viewArray == null) {
            viewArray = new SparseArray<>();
        }

        View view = viewArray.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
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
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (params == null) {
            params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
        }

        if (width != null) {
            params.width = width;
        }
        if (height != null) {
            params.height = height;
        }

        itemView.setLayoutParams(params);
    }

    public void setHolderVerticalMargin(int margin) {
        setHolderMargin(null, margin, null, margin);
    }

    public void setHolderHorizontalMargin(int margin) {
        setHolderMargin(margin, null, margin, null);
    }

    public void setHolderMargin(int margin) {
        setHolderMargin(margin, margin, margin, margin);
    }

    public void setHolderMargin(@Nullable Integer left, @Nullable Integer top, @Nullable Integer right, @Nullable Integer bottom) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (params == null) {
            params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
        }

        if (left != null) {
            params.leftMargin = left;
        }
        if (top != null) {
            params.topMargin = top;
        }
        if (right != null) {
            params.rightMargin = right;
        }
        if (bottom != null) {
            params.bottomMargin = bottom;
        }

        itemView.setLayoutParams(params);
    }

    public void setHolderBackgroundColor(@ColorInt int color) {
        itemView.setBackgroundColor(color);
    }

    public void setHolderBackgroundResource(@DrawableRes int drawableRes) {
        itemView.setBackgroundResource(drawableRes);
    }

    public void setHolderBackgroundDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            itemView.setBackground(drawable);
        } else {
            itemView.setBackgroundDrawable(drawable);
        }
    }

	/*子控件处理*/

    public final void setBackgroundColor(@IdRes int id, @ColorInt int color) {
        View view = getViewById(id);
        view.setBackgroundColor(color);
    }

    public final void setBackgroundResource(@IdRes int id, @DrawableRes int drawableRes) {
        View view = getViewById(id);
        view.setBackgroundResource(drawableRes);
    }

    public final void setBackgroundDrawable(@IdRes int id, Drawable drawable) {
        View view = getViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public final void setImageDrawable(@IdRes int id, Drawable drawable) {
        ImageView imageView = getViewById(id);
        imageView.setImageDrawable(drawable);
    }

    public final void setImageBitmap(@IdRes int id, Bitmap bmp) {
        ImageView imageView = getViewById(id);
        imageView.setImageBitmap(bmp);
    }

    public final void setImageResource(@IdRes int id, @DrawableRes int drawableRes) {
        ImageView imageView = getViewById(id);
        imageView.setImageResource(drawableRes);
    }

    public final void setText(@IdRes int id, @StringRes int stringRes) {
        TextView textView = getViewById(id);
        textView.setText(stringRes);
    }

    public final void setText(@IdRes int id, CharSequence string) {
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
    public final void setOnClickListener(@IdRes int id, View.OnClickListener listener) {
        setOnClickListener(id, listener, true);
    }

    /**
     * 设置id指定控件的点击事件。当autoTag为true时将会将本Holder设置为控件的tag
     *
     * @param id
     * @param listener
     * @param autoTag
     */
    public final void setOnClickListener(@IdRes int id, View.OnClickListener listener, boolean autoTag) {
        View view = getViewById(id);
        view.setOnClickListener(listener);
        if (autoTag) {
            view.setTag(this);
        }
    }

    public final void setVisibility(@IdRes int id, int visibility) {
        View view = getViewById(id);
        view.setVisibility(visibility);
    }

    public final void setVisible(@IdRes int id, boolean visible) {
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
        if (view != itemView) {
            ViewUtil.setSize(view, width, height);
        } else {
            setHolderSize(width, height);
        }
    }
}
