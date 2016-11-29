package core.mate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import core.mate.util.LogUtil;

/**
 * @author DrkCore
 * @since 2016/11/29
 */
public class SimpleViewCreator implements ViewCreator {

    private final Integer layoutId;
    private final Class<? extends View> viewClass;

    public SimpleViewCreator(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        this.viewClass = null;
    }

    public SimpleViewCreator(Class<? extends View> viewClass) {
        this.layoutId = null;
        this.viewClass = viewClass;
    }

    @NonNull
    @Override
    public View create(Context context, LayoutInflater inflater, @Nullable ViewGroup container) {
        if (context == null && inflater == null) {
            throw new IllegalStateException("context与inflater不允许同时为null");
        }

        if (layoutId != null) {
            if (inflater == null) {
                inflater = LayoutInflater.from(context);
            }
            return inflater.inflate(layoutId, container, false);
        } else if (viewClass != null) {
            if (context == null) {
                context = inflater.getContext();
            }
            try {
                Constructor constructor = viewClass.getConstructor(Context.class);
                return (View) constructor.newInstance(context);
            } catch (Exception e) {
                LogUtil.e(e);
                throw new IllegalStateException(e);
            }
        } else {
            throw new IllegalStateException("layoutId和viewClass均不可用");
        }
    }
}
