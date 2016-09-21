package core.mate.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 用于监听文本输入和删除的监听器
 *
 * @author DrkCore
 * @since 2016年2月8日01:12:14
 */
public class EditWatcher implements TextWatcher {

	/*实现*/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count > 0) {
            int end = start + count;
            onTextDelete(s.subSequence(start, end), start, end);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            int end = start + count;
            onTextInput(s.subSequence(start, end), start, end);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

	/*内部回调*/

    protected void onTextInput(CharSequence inputted, int start, int end) {
    }

    protected void onTextDelete(CharSequence deleted, int start, int end) {
    }
}
