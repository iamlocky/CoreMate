package core.mate.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 封装了分隔符处理的ItemOperator
 *
 * @author DrkCore
 * @since 2016年1月18日21:01:24
 */
public final class DividerType extends AbsItemType<Divider> {

	/* 继承 */

    @NonNull
    @Override
    public SimpleViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(new View(parent.getContext()));
    }

    @Override
    public void bindViewData(SimpleViewHolder holder, int position, Divider data) {
        holder.setHolderSize(ViewGroup.LayoutParams.MATCH_PARENT, data.getHeightPx());
        holder.setHolderBackgroundDrawable(data.getDrawable());
    }
}
