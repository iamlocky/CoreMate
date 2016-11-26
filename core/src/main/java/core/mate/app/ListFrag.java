package core.mate.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class ListFrag extends CoreFrag implements OnItemClickListener,
        OnItemLongClickListener {
    
    private int listViewId;
    private int layoutRes;
    
    private ListView listView;
    private BaseAdapter adapter;
    
    public ListView getListView() {
        return listView;
    }
    
    /**
     * 获取{@link ListView#getAdapter()}。
     *
     * @return
     */
    public ListAdapter getRawAdapter() {
        return listView.getAdapter();
    }
    
    /**
     * 获取通过{@link #setAdapter(BaseAdapter)}设置的适配器，
     * 当从未通过上述方法设置适配器时，将会通过{@link #getRawAdapter()}
     * 获取原始的适配器。若原始适配器是{@link HeaderViewListAdapter}
     * 的子类的话，则返回{@link HeaderViewListAdapter#getWrappedAdapter()}。
     * <p>
     * 你也可以通过{@link #getRawAdapter()}从ListView中获取适配器，
     * 但由于Header或者第三方的ListView的原因导致二者的结果可能不一样。
     *
     * @param <T>
     * @return
     */
    public <T extends ListAdapter> T getAdapter() {
        if (adapter != null) {
            return (T) adapter;
        }
        
        ListAdapter adapter = getRawAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) adapter;
            return (T) headerAdapter.getWrappedAdapter();
        } else {
            return (T) listView.getAdapter();
        }
    }
    
    public void configListViewId(@IdRes int listViewId) {
        if (listView != null) {
            throw new IllegalStateException("ListView已经创建，此时无法设置listViewId");
        }
        this.listViewId = listViewId;
    }
    
    public void configLayoutRes(@LayoutRes int layoutRes) {
        if (listView != null) {
            throw new IllegalStateException("ListView已经创建，此时无法设置layoutRes");
        }
        this.layoutRes = layoutRes;
    }
    
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

	/* 继承 */
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView;
        if (layoutRes <= 0) {// 并未指定布局，直接创建ListView
            contentView = new ListView(getActivity());
        } else {// 指定了布局
            contentView = inflater.inflate(layoutRes, container, false);
        }
        
        return contentView;
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof ListView) {// 布局本身就是ListView或者其子类
            listView = (ListView) view;
        } else if (listViewId > 0) {// 指定了id
            listView = (ListView) view.findViewById(listViewId);
        }
        
        if (listView == null) {// 不存在ListView
            throw new IllegalStateException("可用的ListView不存在");
        }
        
        onPrepareListView(listView);
    }
    
	/* 内部回调 */
    
    protected void onPrepareListView(ListView listView) {
        // 设置监听
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
    
	/* 传递ListView的多选操作 */
    
    public boolean isItemChecked(int position) {
        return listView.isItemChecked(position);
    }
    
    public void setItemChecked(int position, boolean value) {
        listView.setItemChecked(position, value);
    }
    
    public <T> T getItemAtPosition(int position) {
        return (T) listView.getItemAtPosition(position);
    }
    
    public long getItemIdAtPosition(int position) {
        return listView.getItemIdAtPosition(position);
    }
    
    public int getAdapterCount() {
        return getAdapter().getCount();
    }
    
    public int getCheckedItemCount() {
        return listView.getCheckedItemIds().length;
    }
    
    public long[] getCheckedItemIds() {
        return listView.getCheckedItemIds();
    }
    
    public int getCheckedItemPosition() {
        return listView.getCheckedItemPosition();
    }
    
    public SparseBooleanArray getCheckedItemPositions() {
        return listView.getCheckedItemPositions();
    }
    
    public void clearChoices() {
        listView.clearChoices();
    }
    
}
