package core.demo.ui.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import core.mate.adapter.SimpleAdapter;
import core.mate.adapter.SimpleViewHolder;
import core.mate.app.ListFrag;
import core.mate.util.ToastUtil;

/**
 * 演示Adapter
 *
 * @author DrkCore
 * @since 2016-9-4
 */
public class AdapterFrag extends ListFrag {

    /*继承*/

    @Override
    protected void onPrepareListView(ListView listView) {
        super.onPrepareListView(listView);
        //创建Adapter，这里可以通过资源id或者类名来自动创建item布局，并处理好ViewHolder的逻辑
        setAdapter(new SimpleAdapter<String>(android.R.layout.simple_list_item_1/*TextView.class*/) {

            @Override
            protected void bindViewData(SimpleViewHolder<String> holder, int position, String data, int viewType) {
                //设置数据可以通过id直接setText
                holder.setText(android.R.id.text1, data);
                //也可以获取到控件后再设置
                //TextView textView = holder.getViewById(android.R.id.text1);
                //textView.setText(data);
            }
        });
    }

    @Override
    public void refresh() {
        super.refresh();
        SimpleAdapter<String> adapter = getAdapter();
        //刷新数据
        adapter.display(new String[]{
                "\\(^o^)/阿萨德撒的萨达哀伤",
                "b(￣▽￣)d",
                "ヾ(≧▽≦*)o",
                "(づ￣3￣)づ╭❤～",
                "_(:зゝ∠)_",
                "(◑▽◐)",
                "哼(ˉ(∞)ˉ)唧"
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        String item = getItemAtPosition(position);
        //简化Toast的使用
        ToastUtil.toastShort(item);
    }
}
