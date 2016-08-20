package core.mate.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * 封装了WebView的Fragment
 *
 * @author DrkCore
 * @since 2016年1月10日10:51:02
 */
public class WebFrag extends CoreFrag {

	/*成员*/

    private int webViewId;
    private int layoutRes;

    private WebView webView;

    protected final WebView getWebView() {
        return webView;
    }

    public final int getWebViewId() {
        return webViewId;
    }

    protected final void configWebViewId(@IdRes int webViewId) {
        if (webView != null) {
            throw new IllegalStateException("WebView已经创建，此时无法设置webViewId");
        }
        this.webViewId = webViewId;
    }

    protected final void configLayoutRes(@LayoutRes int layoutRes) {
        if (webView != null) {
            throw new IllegalStateException("WebView已经创建，此时无法设置layoutRes");
        }
        this.layoutRes = layoutRes;
    }

	/* 继承 */

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView;
        if (layoutRes <= 0) {// 并未指定布局，直接创建WebView
            contentView = new WebView(getActivity());
        } else {// 指定了布局
            contentView = inflater.inflate(layoutRes, container, false);
        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof WebView) {// 布局本身就是WebView或者其子类
            webView = (WebView) view;
        } else if (webViewId > 0) {// 指定了id
            webView = (WebView) view.findViewById(webViewId);
        }

        if (webView == null) {// 不存在webView
            throw new IllegalStateException("可用的WebView不存在");
        }

        onPrepareWebView(webView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            webView.pauseTimers();
        } else {
            webView.resumeTimers();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();

        if (url != null) {
            loadUrl(url);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    /*内部回调*/

    protected void onPrepareWebView(WebView webView) {
    }

    /*拓展*/

    private String url;

    public String getUrl() {
        if (webView != null) {
            url = webView.getUrl();
        }
        return url;
    }

    public void loadUrl(String url) {
        this.url = url;
        if (webView != null && !url.equals(webView.getUrl())) {
            webView.loadUrl(url);
        }
    }
}
