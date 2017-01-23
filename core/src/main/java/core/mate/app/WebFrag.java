package core.mate.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
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

    private int webViewId;
    private int layoutRes;

    private WebView webView;

    public WebView getWebView() {
        return webView;
    }

    public int getWebViewId() {
        return webViewId;
    }

    public void configWebViewId(@IdRes int webViewId) {
        if (webView != null) {
            throw new IllegalStateException("WebView已经创建，此时无法设置webViewId");
        }
        this.webViewId = webViewId;
    }

    public void configLayoutRes(@LayoutRes int layoutRes) {
        if (webView != null) {
            throw new IllegalStateException("WebView已经创建，此时无法设置layoutRes");
        }
        this.layoutRes = layoutRes;
    }

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

    private String beginningUrl;

    public String getBeginningUrl() {
        return beginningUrl;
    }

    public WebFrag setBeginningUrl(String beginningUrl) {
        this.beginningUrl = beginningUrl;
        return this;
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

        if (!TextUtils.isEmpty(beginningUrl)) {
            loadUrl(beginningUrl);
        }
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

    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录

    protected void onPrepareWebView(WebView webView, @Nullable Bundle savedInstanceState) {
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        setLocalCacheEnable(localCacheEnable);
    }

    private boolean localCacheEnable;

    public boolean isLocalCacheEnable() {
        return localCacheEnable;
    }

    public WebFrag setLocalCacheEnable(boolean localCacheEnable) {
        this.localCacheEnable = localCacheEnable;
        if (localCacheEnable && webView != null && webView.getSettings().getCacheMode() != WebSettings.LOAD_CACHE_ELSE_NETWORK) {
            doSetLocalCacheEnable();
        }
        return this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void doSetLocalCacheEnable() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        // 设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
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
