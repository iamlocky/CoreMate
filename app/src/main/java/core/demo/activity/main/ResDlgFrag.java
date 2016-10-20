package core.demo.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import core.demo.R;
import core.mate.app.PanelDlgFrag;
import core.mate.util.RandomUtil;
import core.mate.util.ContextUtil;

/**
 * 从下方弹出对话框的Dlg
 *
 * @author DrkCore
 * @since 2016-09-04
 */
public class ResDlgFrag extends PanelDlgFrag {

    /*继承*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //必须设置高度才能使用
        setDialogHeightPercent(0.7F);//设置高度为屏幕的70%
        //setDialogHeightDp(128);//设置高度为128dp
        return inflater.inflate(R.layout.dlg_res, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnClickListener listener = new View.OnClickListener() {

            private int[] anims = {
                    R.anim.core_slide_left_in,
                    R.anim.core_slide_top_in,
                    R.anim.core_slide_right_in,
                    R.anim.core_slide_bottom_in,

                    R.anim.core_slide_left_out,
                    R.anim.core_slide_top_out,
                    R.anim.core_slide_right_out,
                    R.anim.core_slide_bottom_out
            };

            @Override
            public void onClick(View v) {
                //随机取出动画
                int animId = RandomUtil.randomIn(anims);
                //使用ResUtil实例化动画
                Animation anim = ContextUtil.getAnim(animId);
                //播放动画
                v.startAnimation(anim);
            }
        };

        view.findViewById(R.id.button_dlg_res_anim_1).setOnClickListener(listener);
        view.findViewById(R.id.button_dlg_res_anim_2).setOnClickListener(listener);
        view.findViewById(R.id.button_dlg_res_anim_3).setOnClickListener(listener);
        view.findViewById(R.id.button_dlg_res_anim_4).setOnClickListener(listener);
        view.findViewById(R.id.button_dlg_res_anim_5).setOnClickListener(listener);
    }
}
