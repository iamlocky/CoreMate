package core.mate.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

public abstract class SimpleRecyclerAdapter<Item> extends CoreRecyclerAdapter<Item, SimpleRecyclerViewHolder> {

    private final ViewCreator viewCreator;

    public SimpleRecyclerAdapter(@LayoutRes int layoutId) {
        this(new SimpleViewCreator(layoutId));
    }

    public SimpleRecyclerAdapter(@LayoutRes int layoutId, Item[] itemArr) {
        this(new SimpleViewCreator(layoutId), itemArr);
    }

    public SimpleRecyclerAdapter(@LayoutRes int layoutId, Collection<Item> items) {
        this(new SimpleViewCreator(layoutId), items);
    }

    public SimpleRecyclerAdapter(Class<? extends View> viewClass) {
        this(new SimpleViewCreator(viewClass));
    }

    public SimpleRecyclerAdapter(Class<? extends View> viewClass, Item[] itemArr) {
        this(new SimpleViewCreator(viewClass), itemArr);
    }

    public SimpleRecyclerAdapter(Class<? extends View> viewClass, Collection<Item> items) {
        this(new SimpleViewCreator(viewClass), items);
    }

    public SimpleRecyclerAdapter(ViewCreator viewCreator) {
        this.viewCreator = viewCreator;
    }

    public SimpleRecyclerAdapter(ViewCreator viewCreator, Item[] itemArr) {
        super(itemArr);
        this.viewCreator = viewCreator;
    }

    public SimpleRecyclerAdapter(ViewCreator viewCreator, Collection<Item> items) {
        super(items);
        this.viewCreator = viewCreator;
    }

	/*继承*/

    @NonNull
    @Override
    protected final SimpleRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int type) {
        View view = viewCreator.create(getContext(), inflater, parent);
        SimpleRecyclerViewHolder viewHolder = new SimpleRecyclerViewHolder(view);
        onViewHolderCreated(viewHolder, type);
        return viewHolder;
    }

    protected void onViewHolderCreated(SimpleRecyclerViewHolder holder, int viewType) {

    }
}
