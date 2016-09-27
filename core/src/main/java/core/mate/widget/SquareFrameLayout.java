package core.mate.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class SquareFrameLayout extends FrameLayout {

	public SquareFrameLayout(Context context) {
		super(context);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/* 继承 */

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		int childWidthSize = getMeasuredWidth();
		heightMeasureSpec = widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}