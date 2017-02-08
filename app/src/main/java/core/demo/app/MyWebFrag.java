package core.demo.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import core.mate.app.WebFrag;

/**
 * @author DrkCore
 * @since 2017/1/23
 */
public class MyWebFrag extends WebFrag {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocalCacheEnable(true);
        setBeginningUrl("https://www.baidu.com/");
    }
}
