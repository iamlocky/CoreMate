package core.mate.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author DrkCore
 * @since 2016-09-21
 */
public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private int offset;

    public int getOffset() {
        return offset;
    }

    public OffsetDecoration setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public OffsetDecoration() {
    }

    public OffsetDecoration(int offset) {
        this.offset = offset;
    }

    /*继承*/

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = offset;
        outRect.top = offset;
        outRect.right = offset;
        outRect.bottom = offset;
    }
}
