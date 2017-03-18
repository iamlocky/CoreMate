package core.mate.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import core.mate.util.LogUtil;
import core.mate.util.ViewUtil;
import core.mate.view.ITaskIndicator;

/**
 * 进度条对话框
 *
 * @author DrkCore 178456643@qq.com
 * @since 2015年10月29日13:20:25s
 */
public class ProgressDlgFrag extends CoreDlgFrag implements ITaskIndicator {

	/* 继承 */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ProgressBar progressBar = new ProgressBar(
                new ContextThemeWrapper(getContext(), android.R.style.Widget_DeviceDefault_ProgressBar_Large), null, android.R.style.Widget_DeviceDefault_ProgressBar_Large);

        LinearLayout linearLayout = new LinearLayout(getContext());
        int dp8 = ViewUtil.dpToPx(8);
        linearLayout.setPadding(dp8, dp8, dp8, dp8);
        linearLayout.setBackgroundColor(Color.WHITE);

        linearLayout.addView(progressBar);

        return linearLayout;
    }

	/* ITaskIndicator */

    private FragmentManager fragMgr;
    private boolean progressing;

    public ProgressDlgFrag setFragmentManager(Fragment frag) {
        return setFragmentManager(frag.getChildFragmentManager());
    }

    public ProgressDlgFrag setFragmentManager(FragmentActivity fragAct) {
        return setFragmentManager(fragAct.getSupportFragmentManager());
    }

    public ProgressDlgFrag setFragmentManager(FragmentManager fragMgr) {
        this.fragMgr = fragMgr;
        return this;
    }

    @Override
    public boolean isProgressing() {
        return progressing;
    }

    @Override
    public void showProgress() {
        progressing = true;
        //阻塞用户操作
        setCancelable(false);
        setCancelOnTouchOutSideEnable(false);
        show(fragMgr);
    }

    @Override
    public void hideProgress() {
        try {
            dismiss();
        } catch (Exception e) {
            LogUtil.e(e);
        }
        progressing = false;
    }
}
