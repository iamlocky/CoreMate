package core.mate.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author DrkCore
 * @since 2016年2月1日00:06:31
 */
public abstract class SimpleRecyclerType<Item> extends AbsRecyclerItemType<Item> {

    private final ViewCreator viewCreator;

    public SimpleRecyclerType(@LayoutRes int layoutId) {
        this(new SimpleViewCreator(layoutId));
    }

    public SimpleRecyclerType(Class<? extends View> viewClass) {
        this(new SimpleViewCreator(viewClass));
    }

    public SimpleRecyclerType(ViewCreator viewCreator) {
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

    @Override
    public final SimpleRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        if (this.context == null) {
            context = inflater.getContext();
            this.inflater = inflater;
        }

        View view = viewCreator.create(getContext(), inflater, parent);
        SimpleRecyclerViewHolder holder = new SimpleRecyclerViewHolder(view);
        onViewHolderCreated(holder);
        return holder;
    }

    public void onViewHolderCreated(SimpleRecyclerViewHolder holder) {

    }

    @Override
    public void bindViewData(SimpleRecyclerViewHolder holder, int position, Item data) {

    }

    public int getPositionFromView(View view) {
        Object tag = view.getTag();
        if (tag instanceof SimpleRecyclerViewHolder) {
            return ((SimpleRecyclerViewHolder) tag).getAdapterPosition();
        }
        return ListView.INVALID_POSITION;
    }

    @Nullable
    public <T> T getItemFromView(View view) {
        int position = getPositionFromView(view);
        if (position >= 0 && getAdapter() != null && getAdapter().getItemCount() > position) {
            return (T) getAdapter().getItem(position);
        }
        return null;
    }
}
