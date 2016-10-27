package core.mate.view;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

/**
 * @author DrkCore
 * @since 2016-10-27
 */
public class NestedScrollerBinder extends ScrollBinder implements NestedScrollView.OnScrollChangeListener {

    public NestedScrollerBinder(View view) {
        super(view);
    }

    public NestedScrollerBinder(View view, float outY) {
        super(view, outY);
    }

    public NestedScrollerBinder(View view, float outY, float factor) {
        super(view, outY, factor);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        onScroll(scrollY);
    }
}
