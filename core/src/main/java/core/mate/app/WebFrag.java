package core.mate.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

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

    public WebView getWebView() {
        return webView;
    }

    public int getWebViewId() {
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

        onPrepareWebView(webView, savedInstanceState);
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

    protected void onPrepareWebView(WebView webView, @Nullable Bundle savedInstanceState) {
    }

    /*拓展*/

    @ViewDebug.ExportedProperty(category = "webview")
    public String getUrl() {
        return webView.getUrl();
    }

    @ViewDebug.ExportedProperty(category = "webview")
    public String getOriginalUrl() {
        return webView.getOriginalUrl();
    }

    @ViewDebug.ExportedProperty(category = "webview")
    public String getTitle() {
        return webView.getTitle();
    }

    public Bitmap getFavicon() {
        return webView.getFavicon();
    }

    public int getProgress() {
        return webView.getProgress();
    }

    @ViewDebug.ExportedProperty(category = "webview")
    public int getContentHeight() {
        return webView.getContentHeight();
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        webView.loadUrl(url, additionalHttpHeaders);
    }

    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    public void reload() {
        webView.reload();
    }

    public void stopLoading() {
        webView.stopLoading();
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }

    public boolean canGoForward() {
        return webView.canGoForward();
    }

    public void goForward() {
        webView.goForward();
    }

    public boolean canGoBackOrForward(int steps) {
        return webView.canGoBackOrForward(steps);
    }

    public void goBackOrForward(int steps) {
        webView.goBackOrForward(steps);
    }

    public boolean pageUp(boolean top) {
        return webView.pageUp(top);
    }

    public boolean pageDown(boolean bottom) {
        return webView.pageDown(bottom);
    }

    public void setWebChromeClient(WebChromeClient client) {
        webView.setWebChromeClient(client);
    }

    public void setWebViewClient(WebViewClient client) {
        webView.setWebViewClient(client);
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        webView.addJavascriptInterface(object, name);
    }

    public void clearHistory() {
        webView.clearHistory();
    }

    public void setWebViewVisible(boolean visible) {
        webView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
