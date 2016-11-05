package core.mate.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import core.mate.util.DataUtil;

/**
 * @author DrkCore
 * @since 2016年2月10日22:07:37
 */
public abstract class CorePagerAdapter<Item> extends PagerAdapter {

    private final List<Item> data;

    public CorePagerAdapter() {
        data = new ArrayList<>();
    }

    public CorePagerAdapter(Collection<Item> items) {
        int size = DataUtil.getSize(items);
        data = new ArrayList<>(size);
        if (size > 0) {
            data.addAll(items);
        }
    }

    @SafeVarargs
    public CorePagerAdapter(Item... items) {
        int size = DataUtil.getSize(items);
        data = new ArrayList<>(size);
        if (size > 0) {
            Collections.addAll(data, items);
        }
    }

	/*继承*/

    private LayoutInflater inflater;
    private SparseArray<View> views;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(container.getContext());
        }
        if (views == null) {
            views = new SparseArray<>(getCount());
        }

        //检查对应视图是否已存在
        View view = views.get(position);
        if (view == null) {
            Item data = getItem(position);
            view = onCreateItemView(inflater, container, position, data);

            views.put(position, view);

            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views != null ? views.get(position) : null;
        if (view != null) {
            container.removeView(view);
            views.remove(position);
        }
    }

    private boolean nonePositionEnable = true;

    public boolean isNonePositionEnable() {
        return nonePositionEnable;
    }

    /**
     * 是否在{@link #getItemPosition(Object)}返回{@link #POSITION_NONE}。
     * <p>
     * 启用后{@link #notifyDataSetChanged()}才能触发{@link #notifyDataSetChanged()}效果。
     * <p>
     * 默认启用。
     *
     * @param nonePositionEnable
     * @return
     */
    public CorePagerAdapter setNonePositionEnable(boolean nonePositionEnable) {
        this.nonePositionEnable = nonePositionEnable;
        return this;
    }

    @Override
    public int getItemPosition(Object object) {
        return nonePositionEnable ? POSITION_NONE : super.getItemPosition(object);
    }

	/*内部回调*/

    @NonNull
    protected abstract View onCreateItemView(LayoutInflater inflater, ViewGroup container, int position, Item data);

	/*项目处理*/

    public void display(Item... items) {
        this.data.clear();
        if (!DataUtil.isEmpty(items)) {
            Collections.addAll(this.data, items);
        }
        notifyDataSetChanged();
    }

    public void display(Collection<? extends Item> items) {
        this.data.clear();
        if (!DataUtil.isEmpty(items)) {
            this.data.addAll(items);
        }
        notifyDataSetChanged();
    }

    public boolean add(Item... items) {
        if (!DataUtil.isEmpty(items) && Collections.addAll(this.data, items)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean add(Collection<? extends Item> items) {
        if (!DataUtil.isEmpty(items) && data.addAll(items)) {
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

    public final Item getItem(int position) {
        return data.get(position);
    }

    public final Iterator<Item> iterator() {
        return data.iterator();
    }

    public final int indexOf(Item item) {
        return data.indexOf(item);
    }
}
