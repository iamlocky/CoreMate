package core.mate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author DrkCore
 * @since 2016年1月18日20:53:04
 */
public abstract class SimpleType<Item> extends AbsItemType<Item> {

    private final ViewCreator viewCreator;

    public SimpleType(@LayoutRes int layoutId) {
        this(new SimpleViewCreator(layoutId));
    }

    public SimpleType(Class<? extends View> viewClass) {
        this(new SimpleViewCreator(viewClass));
    }

    public SimpleType(ViewCreator viewCreator) {
        this.viewCreator = viewCreator;
    }

    private Context context;
    private LayoutInflater inflater;

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @NonNull
    @Override
    public final SimpleViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        if (this.context == null) {
            context = inflater.getContext();
            this.inflater = inflater;
        }

        View view = viewCreator.create(getContext(), inflater, parent);
        SimpleViewHolder viewHolder = new SimpleViewHolder(view);
        onViewHolderCreated(viewHolder);
        return viewHolder;
    }

    protected void onViewHolderCreated(SimpleViewHolder holder) {

    }

    @Nullable
    protected <Item> Item getItemFromView(View view) {
        if (view.getTag() instanceof SimpleViewHolder) {
            int position = ((SimpleViewHolder) view.getTag()).getPosition();
            if (position >= 0 && getAdapter() != null && getAdapter().getCount() > position) {
                return (Item) getAdapter().getItem(position);
            }
        }
        return null;
    }
}
