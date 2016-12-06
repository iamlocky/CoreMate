package core.mate.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @param <Item>
 * @author DrkCore
 * @since 2015年12月2日19:38:24
 */
public abstract class CoreRecyclerAdapter<Item> extends RecyclerView.Adapter<SimpleRecyclerViewHolder> implements OnClickListener, OnLongClickListener {

    private final ArrayList<Item> data = new ArrayList<>();

    public CoreRecyclerAdapter() {
    }

    @SuppressWarnings("unchecked")
    public CoreRecyclerAdapter(Item... items) {
        if (items != null) {
            Collections.addAll(this.data, items);
        }
    }

    public CoreRecyclerAdapter(Collection<Item> items) {
        if (items != null) {
            this.data.addAll(items);
        }
    }

    private Context context;
    private LayoutInflater inflater;
    private ViewGroup viewGroup;

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public final SimpleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
            inflater = LayoutInflater.from(context);
            viewGroup = parent;
        }
        SimpleRecyclerViewHolder holder = createViewHolder(inflater, parent, viewType);
        // 绑定事件
        if (!(holder.itemView instanceof AdapterView)//不是AdapterView
                && !(holder.itemView instanceof RecyclerView)//不是RecyclerView
                && !(holder.itemView instanceof ViewPager)//不是ViewPager
                ) {
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
        }
        return holder;
    }

    @Override
    public final void onBindViewHolder(SimpleRecyclerViewHolder holder, int position) {
        Item item = getItem(position);
        bindViewData(holder, position, item, getItemViewType(position));
    }

	/* 内部回调 */

    @NonNull
    protected abstract SimpleRecyclerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int type);

    protected abstract void bindViewData(SimpleRecyclerViewHolder holder, int position, Item data, int viewType);

	/* 接口实现 */

    @CallSuper
    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof RecyclerView.ViewHolder && onItemClickListener != null) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) tag;
            int pos = viewHolder.getAdapterPosition();
            if (pos >= 0) {
                onItemClickListener.onItemClick(viewGroup, v, pos);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Object tag = v.getTag();
        if (tag instanceof RecyclerView.ViewHolder && onItemLongClickListener != null) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) tag;
            int pos = viewHolder.getAdapterPosition();
            if (pos >= 0) {
                return onItemLongClickListener.onItemLongClick(viewGroup, v, pos);
            }
        }
        return false;
    }

	/* 外部接口 */

    public interface OnItemClickListener {

        void onItemClick(ViewGroup parent, View v, int adapterPosition);

    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(ViewGroup parent, View v, int adapterPosition);

    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    protected OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    protected OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public CoreRecyclerAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public CoreRecyclerAdapter setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
        return this;
    }

	/* 数据处理 */

    public void display(Item... items) {
        this.data.clear();
        if (items != null) {
            Collections.addAll(this.data, items);
        }
        notifyDataSetChanged();
    }

    public void display(Collection<Item> items) {
        this.data.clear();
        if (items != null) {
            this.data.addAll(items);
        }
        notifyDataSetChanged();
    }

    public boolean add(Item... items) {
        if (items != null && Collections.addAll(this.data, items)) {
            notifyItemRangeInserted(this.data.size() - items.length, items.length);
            return true;
        }
        return false;
    }

    public boolean add(Collection<Item> items) {
        if (items != null && this.data.addAll(items)) {
            notifyItemRangeInserted(this.data.size() - items.size(), items.size());
            return true;
        }
        return false;
    }

    public boolean remove(Item item) {
        int idx = this.data.indexOf(item);
        return remove(idx);
    }

    public boolean remove(int position) {
        if (this.data.remove(position) != null) {
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

	/*拓展*/

    public boolean refresh(Item item) {
        int pos = data.indexOf(item);
        if (pos >= 0) {
            data.set(pos, item);
            notifyItemChanged(pos);
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    protected List<Item> getSrcData() {
        return data;
    }

    /**
     * 浅复制一个和原始数据一样的列表。
     *
     * @return
     */
    public List<Item> getData() {
        return new ArrayList<>(data);
    }

    public Iterator<Item> getIterator() {
        return data.iterator();
    }

    public Item getItem(int position) {
        return data.get(position);
    }

    public int indexOf(Item item) {
        return data.indexOf(item);
    }

    public boolean contains(Item item) {
        return data.contains(item);
    }
}

