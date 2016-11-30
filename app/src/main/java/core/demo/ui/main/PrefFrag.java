package core.demo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import core.demo.R;
import core.demo.helper.ConfigHelper;
import core.mate.app.CoreFrag;

/**
 * 演示SharePreference
 *
 * @author DrkCore
 * @since 2016-09-04
 */
public class PrefFrag extends CoreFrag {

    /*继承*/

    private EditText editText;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_pref, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = (EditText) view.findViewById(R.id.editText_frag_pref);
        textView = (TextView) view.findViewById(R.id.textView_frag_pref_result);

        view.findViewById(R.id.button_frag_pref_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                ConfigHelper.setConfigParams(text);

                String rawParams = ConfigHelper.getRawConfigParams();
                String params = ConfigHelper.getConfigParams();

                CharSequence result = TextUtils.concat("加密后的字符串 = \n", rawParams, "\n\n解密后的字符串 = \n", params);
                textView.setText(result);
            }
        });
    }
}
