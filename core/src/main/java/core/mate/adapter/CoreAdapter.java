package core.mate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 初步封装的Adapter基类。
 *
 * @param <Item>
 * @author DrkCore
 * @since 2015年10月31日23:37:53
 */
public abstract class CoreAdapter<Item, Holder extends AbsViewHolder<Item>> extends BaseAdapter {

	private final List<Item> data = new ArrayList<>();

	public CoreAdapter() {
	}

	@SuppressWarnings("unchecked")
	public CoreAdapter(Item... itemArr) {
		if (itemArr != null) {
			Collections.addAll(this.data, itemArr);
		}
	}

	public CoreAdapter(Collection<Item> items) {
		if (items != null) {
			this.data.addAll(items);
		}
	}

	/* 继承 */

	private Context context;
	private LayoutInflater inflater;

	public final Context getContext() {
		return context;
	}

	public final LayoutInflater getInflater() {
		return inflater;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Item getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		if (context == null) {
			context = parent.getContext();
			inflater = LayoutInflater.from(parent.getContext());
		}
		int viewType = getItemViewType(position);
		Item data = getItem(position);

		Holder holder;
		if (convertView == null) {
			holder = createViewHolder(inflater, parent, viewType);
			convertView = holder.getView();
			convertView.setTag(holder);// 绑定ViewHolder
		} else {
			holder = (Holder) convertView.getTag();
		}
		// 绑定视图数据
		holder.setPosition(position);
		holder.setItem(data);
		bindViewData(holder, position, data, viewType);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		super.hasStableIds();
		// 钩子方法，使之返回true。否者多选操作会bug
		return true;
	}

	/* 内部回调 */

	@NonNull
	protected abstract Holder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

	protected abstract void bindViewData(Holder holder, int position, Item data, int viewType);

	/* 数据展示 */

	public void display(Item... items) {
		this.data.clear();
		if (items != null) {
			Collections.addAll(this.data, items);
		}
		notifyDataSetChanged();
	}

	public void display(Collection<? extends Item> items) {
		this.data.clear();
		if (items != null) {
			this.data.addAll(items);
		}
		notifyDataSetChanged();
	}

	public boolean add(Item... items) {
		if (items != null && Collections.addAll(this.data, items)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public boolean add(Collection<? extends Item> items) {
		if (items != null && data.addAll(items)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public boolean remove(int position) {
		if (this.data.remove(position) != null) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public boolean remove(Item item) {
		if (this.data.remove(item)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public boolean remove(Collection<? extends Item> items) {
		if (this.data.removeAll(items)) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public void clear() {
		this.data.clear();
		notifyDataSetChanged();
	}

	/*拓展*/

	/**
	 * 浅复制一个和原始数据一样的列表。
	 *
	 * @return
	 */
	public final List<Item> cloneData() {
		return new ArrayList<>(data);
	}

	public final Iterator<Item> iterator() {
		return data.iterator();
	}

	public final int indexOf(Item item) {
		return data.indexOf(item);
	}

	public final boolean contains(Item item) {
		return data.contains(item);
	}

}
