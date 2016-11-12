package core.mate.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

import core.mate.R;
import core.mate.util.ViewUtil;

/**
 * 面板对话框。
 *
 * 否则将默认使用{@link LayoutParams#WRAP_CONTENT}作为高度。
 * 这将导致的结果就是所加载的布局的高度全部转为wrap_content，
 * 尽管其layout_height属性可能是match_parent或者是某个具体的dp数值。
 *
 * @author DrkCore
 * @since 2015年10月27日00:23:17
 */
public class PanelDlgFrag extends CoreDlgFrag {

	/* 继承 */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPrepareDialogStyle(savedInstanceState);
    }

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {// 使用final修饰阻止重写该方法
        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * 配置DialogFragment的样式，在{@link #onCreate(Bundle)}中回调。
     * 默认将会设置样式为{@link #STYLE_NO_FRAME}，并且style为
     * {@link core.mate.R.style#PanelDlgStyle}
     *
     * @param savedInstanceState
     */
    protected void onPrepareDialogStyle(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, R.style.PanelDlgStyle);
        setWinAnimStyle(R.style.CoreWindowAnimSlideTopStyle);
        setWidth(LayoutParams.MATCH_PARENT);
        setGravity(Gravity.BOTTOM);
    }

    /**
     * 设置对话框的高度相对于设备高度的百分比。
     *
     * @param percent [0,1]
     * @return
     * @throws IllegalArgumentException 当percent大于1或者小于0时抛出该异常
     */
    protected final PanelDlgFrag setDialogHeightPercent(float percent) {
        if (percent < 0 || percent > 1) {
            throw new IllegalArgumentException("percent " + percent + " 不合法");
        }
        setHeight((int) (ViewUtil.getScreenHeightPx() * percent));
        return this;
    }

}
