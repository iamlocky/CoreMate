package core.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import core.demo.R;
import core.demo.async.ReadAssetsMgr;
import core.mate.adapter.CoreRecyclerAdapter;
import core.mate.adapter.SimpleRecyclerAdapter;
import core.mate.adapter.SimpleRecyclerViewHolder;
import core.mate.async.AsyncManager;
import core.mate.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    /*继承*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new SimpleRecyclerAdapter<String>(TextView.class, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10") {
            @Override
            protected void bindViewData(SimpleRecyclerViewHolder viewHolder, int position, String data, int viewType) {
                TextView textView = viewHolder.getCastView();
                textView.setBackgroundColor(Color.RED);
                textView.setText(data);
            }
        }.setOnItemClickListener(new CoreRecyclerAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View v, int adapterPosition, String s) {
                testAsyncMgr();
            }
        }));
    }

    private void testAsyncMgr() {
        new ReadAssetsMgr().setUp("test.txt", getFilesDir()).setOnAsyncListener(new AsyncManager.OnAsyncListener<ReadAssetsMgr>() {
            @Override
            public void onAsyncStateChanged(ReadAssetsMgr readAssetsMgr, int state) {
                if (state == AsyncManager.STATE_FINISH) {
                    String content = readAssetsMgr.getResult();
                    ToastUtil.toastShort(content);
                }
            }
        }).start();
    }
}
