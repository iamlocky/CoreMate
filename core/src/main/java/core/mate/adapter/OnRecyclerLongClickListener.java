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
        return onItemLongClick(parent instanceof RecyclerView ? (RecyclerView) parent : null, v, adapterPosition);
    }

    public abstract boolean onItemLongClick(RecyclerView recyclerView, View v, int adapterPosition);

}
