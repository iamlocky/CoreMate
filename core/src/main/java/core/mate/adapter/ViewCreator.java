package core.mate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author DrkCore
 * @since 2016/11/29
 */
public interface ViewCreator {

    /**
     * context和inflater不允许同时为空
     *
     * @param context
     * @param inflater
     * @param container
     * @return
     */
    @NonNull
    View create(Context context, LayoutInflater inflater, @Nullable ViewGroup container);

}
