package core.mate.view;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

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
		int curScrollY = getListViewScrollY(view);
		onScroll(curScrollY);
	}

	/**
	 * 获取ListView的滚动的Y。该方法只适用于Item的高度都一致的情况
	 *
	 * @param listView
	 * @return
	 */
	public static int getListViewScrollY(AbsListView listView) {
		//这里获得并不是Adapter中第0个item而是ListView可见范围内的第0个item
		View lastVisibleItem = listView.getChildAt(0);
		if (lastVisibleItem == null) {
			return 0;
		}
		//初始状态下第0个和firstVisible一样的
		//但是滑动到下面之后第0个其实是firstVisible的上一个item
		int firstVisiblePosition = listView.getFirstVisiblePosition();
		int top = lastVisibleItem.getTop();
		//综合计算一下就可以得出当前Y，不过只限于Item的高度都是一样的情况下
		int scrollY = -top + firstVisiblePosition * lastVisibleItem.getHeight();

		if (listView instanceof ListView) {
			ListView view = (ListView) listView;
			int dividerHeight = view.getDividerHeight();
			scrollY += dividerHeight * firstVisiblePosition;
		}
		return scrollY;
	}
}
