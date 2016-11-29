package core.mate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * @author DrkCore
 * @since 2016年1月18日20:47:27
 */
public abstract class SimpleAdapter<Item> extends CoreAdapter<Item, SimpleViewHolder<Item>> {

    private final ViewCreator viewCreator;

    public SimpleAdapter(@LayoutRes int layoutId) {
        this(new SimpleViewCreator(layoutId));
    }

    public SimpleAdapter(@LayoutRes int layoutId, Item[] itemArr) {
        this(new SimpleViewCreator(layoutId), itemArr);
    }

    public SimpleAdapter(@LayoutRes int layoutId, Collection<Item> items) {
        this(new SimpleViewCreator(layoutId), items);
    }

    public SimpleAdapter(Class<? extends View> viewClass) {
        this(new SimpleViewCreator(viewClass));
    }

    public SimpleAdapter(Class<? extends View> viewClass, Item[] itemArr) {
        this(new SimpleViewCreator(viewClass), itemArr);
    }

    public SimpleAdapter(Class<? extends View> viewClass, Collection<Item> items) {
        this(new SimpleViewCreator(viewClass), items);
    }

    public SimpleAdapter(ViewCreator viewCreator) {
        this.viewCreator = viewCreator;
    }

    public SimpleAdapter(ViewCreator viewCreator, Item[] itemArr) {
        super(itemArr);
        this.viewCreator = viewCreator;
    }

    public SimpleAdapter(ViewCreator viewCreator, Collection<Item> items) {
        super(items);
        this.viewCreator = viewCreator;
    }

    /*继承*/

    @NonNull
    @Override
    protected final SimpleViewHolder<Item> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = viewCreator.create(getContext(), inflater, parent);
        SimpleViewHolder<Item> viewHolder = new SimpleViewHolder<>(view);
        onViewHolderCreated(viewHolder, viewType);
        return viewHolder;
    }

    protected void onViewHolderCreated(SimpleViewHolder<Item> holder, int viewType) {

    }

}
