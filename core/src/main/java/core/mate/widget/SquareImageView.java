package core.mate.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

	public SquareImageView(Context context) {
		super(context);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/* 继承 */

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

		int childWidth = getMeasuredWidth();
		int childHeight = getMeasuredHeight();

		// 使Layout的高度等于宽度
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
