package core.mate.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import core.mate.R;

public class RatioImageView extends AppCompatImageView {

	public static final int DEFAULT_RATIO = 1;

	public RatioImageView(Context context) {
		this(context, null);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Ratio);
		ratio = array.getFloat(R.styleable.Ratio_ratio, DEFAULT_RATIO);
		array.recycle();
	}

	private float ratio;

	public float getRatio() {
		return ratio;
	}

	public RatioImageView setRatio(float ratio) {
		if (this.ratio != ratio) {
			this.ratio = ratio;
			performRatioChange();
		}
		return this;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		int width = getMeasuredWidth();
		int height = (int) (width * ratio);
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void performRatioChange() {
		requestLayout();
		if (listener != null) {
			listener.onRatioChanged(ratio);
		}
	}

	public interface OnRatioChangedListener {

		void onRatioChanged(float ratio);

	}

	private RatioImageView.OnRatioChangedListener listener;

	public RatioImageView setOnRatioChangedListener(RatioImageView.OnRatioChangedListener listener) {
		this.listener = listener;
		return this;
	}
}
