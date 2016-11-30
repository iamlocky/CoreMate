package core.demo.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import core.demo.R;
import core.demo.ui.main.AdapterFrag;
import core.demo.ui.main.PrefFrag;
import core.demo.ui.main.ResDlgFrag;
import core.demo.ui.main.TaskFrag;
import core.mate.app.CoreActivity;

public class MainActivity extends CoreActivity {

    /*继承*/

    private TabLayout tabLayout;
    private Fragment curFrag;

    private static final String KEY_TAB_IDX = "KEY_TAB_IDX";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TAB_IDX, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_main_tabs);

        //填充TabLayout
        Class[] frags = {AdapterFrag.class, TaskFrag.class, PrefFrag.class};
        for (Class clz : frags) {
            tabLayout.addTab(tabLayout.newTab().setText(clz.getSimpleName()).setTag(clz));
        }

        TabLayout.OnTabSelectedListener listener;
        tabLayout.addOnTabSelectedListener(listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //使用FragHelper快速切换Fragment
                //CoreFrag已经处理了重建Activity后Frag重叠的问题
                Class clz = (Class) tab.getTag();
                curFrag = getFragHelper().switchFrag(R.id.frameLayout_main_fragContainer, curFrag, clz);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int position = savedInstanceState == null ? 0 : savedInstanceState.getInt(KEY_TAB_IDX);
        tabLayout.getTabAt(position).select();
        listener.onTabSelected(tabLayout.getTabAt(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("查看资源");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        new ResDlgFrag().show(this);
        return true;
    }
}
