package core.mate.view;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * 当适配器为空时切换空视图可见性的观察者
 *
 * @author DrkCore
 * @since 2016年9月28日11:20:50
 */
public class EmptyBinder {

    public static void bind(BaseAdapter adapter, View... emptyViews) {
        EmptyBinder observer = new EmptyBinder(adapter, null, emptyViews);
        adapter.registerDataSetObserver(observer.getListAdapterObserver());
    }

    public static void bind(RecyclerView.Adapter<?> adapter, View... emptyViews) {
        EmptyBinder observer = new EmptyBinder(null, adapter, emptyViews);
        adapter.registerAdapterDataObserver(observer.getRecyclerAdapterObserver());
    }

    private final View[] emptyViews;

    private EmptyBinder(BaseAdapter baseAdapter, RecyclerView.Adapter<?> recyclerAdapter, View[] emptyViews) {
        this.baseAdapter = baseAdapter;
        this.recyclerAdapter = recyclerAdapter;
        this.emptyViews = emptyViews;
    }

    private void onChanged() {
        int visible = View.VISIBLE;
        if (baseAdapter != null && baseAdapter.isEmpty()
                || (recyclerAdapter != null && recyclerAdapter.getItemCount() == 0)) {
            visible = View.INVISIBLE;
        }

        for (View view : emptyViews) {
            view.setVisibility(visible);
        }
    }

	/* BaseAdapter */

    private class ListAdapterObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            EmptyBinder.this.onChanged();
        }

    }

    private final BaseAdapter baseAdapter;
    private ListAdapterObserver listAdapterObserver;

    private ListAdapterObserver getListAdapterObserver() {
        return listAdapterObserver != null ? listAdapterObserver : (listAdapterObserver = new ListAdapterObserver());
    }

	/* RecyclerAdapter */

    private class RecyclerAdapterObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            EmptyBinder.this.onChanged();
        }

    }

    private final RecyclerView.Adapter<?> recyclerAdapter;
    private RecyclerAdapterObserver recyclerAdapterObserver;

    private RecyclerAdapterObserver getRecyclerAdapterObserver() {
        return recyclerAdapterObserver != null ? recyclerAdapterObserver
                : (recyclerAdapterObserver = new RecyclerAdapterObserver());
    }

}
