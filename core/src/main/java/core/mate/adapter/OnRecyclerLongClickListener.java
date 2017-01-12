package core.mate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author DrkCore
 * @since 2016/12/6
 */
public abstract class OnRecyclerLongClickListener implements CoreRecyclerAdapter.OnItemLongClickListener {

    @Override
    public boolean onItemLongClick(ViewGroup parent, View v, int adapterPosition) {
        return onItemLongClick(parent instanceof RecyclerView ? (RecyclerView) parent : null, v, adapterPosition, getItemAt(parent, adapterPosition));
    }

    public <T> T getItemAt(ViewGroup parent, int adapterPosition) {
        T t = null;
        if (parent instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) parent).getAdapter();
            if (adapter instanceof CoreRecyclerAdapter) {
                t = (T) ((CoreRecyclerAdapter) adapter).getItem(adapterPosition);
            }
        }
        return t;
    }

    public abstract boolean onItemLongClick(RecyclerView recyclerView, View v, int adapterPosition, Object item);

}
