package core.mate.view;

import android.view.View;
import android.widget.AbsListView;

import core.mate.util.ViewUtil;

/**
 * @author DrkCore
 * @since 2016-09-28
 */
public class ListScrollBinder extends ScrollBinder implements AbsListView.OnScrollListener {

	public ListScrollBinder(View view) {
		super(view);
	}

	public ListScrollBinder(View view, float outY, float factor) {
		super(view, outY, factor);
	}

	/*回调滚动*/

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			onFinishScroll();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int curScrollY = ViewUtil.getListViewScrollY(view);
		onScroll(curScrollY);
	}
}
