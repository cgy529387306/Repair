package com.yxw.cn.carpenterrepair.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;

import butterknife.BindView;

/**
 * Web
 */
public class WebFragment extends BaseFragment {

    @BindView(R.id.webView)
    WebView mWebView;

    private String url;
    private boolean loaded = false;
    private Gson gson = new Gson();

    public static WebFragment newInstance(String url) {
        WebFragment f = new WebFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayout() {
        return R.layout.frg_web;
    }

    @Override
    public void initData() {
        url = getArguments().getString("url");
    }

    @Override
    public void initView() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebView.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        mWebView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        mWebView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mWebView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setDomStorageEnabled(true);//DOM Storage
        mWebView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);//设置缓冲大小，我设的是8M
        String appCachePath = mContext.getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
//        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.setDrawingCacheEnabled(true);
    /*    mWebView.addJavascriptInterface(new WebJsInterface(), "webExternal");
        if (FLAG_WEBVIEW_DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }*/
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    @Override
    public void getData() {
        load();
    }

    public void load() {
        mWebView.loadUrl(url);
    }

    public boolean canGoBack(){
        return mWebView.canGoBack();
    }

    public void goBack(){
        mWebView.goBack();
    }

/*    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.USE_JS:
                JsMessage jsMessage = (JsMessage) event.getData();
                if (mWebView != null) {
                    mWebView.loadUrl("javascript:AppMessage('" + jsMessage.getType() + "','" + jsMessage.getContnet() + "')");
                }
                break;
            case MessageConstant.USE_JS_HANDWRITTEN:
                if (event.isSuccess()) {
                    if (TextUtils.isEmpty(event.getData().toString())) {
                        mWebView.loadUrl("javascript:HandWriting_After()");
                    } else {
                        mWebView.loadUrl("javascript:HandWriting_After('" + event.getData().toString() + "')");
                    }
                } else {
                    toast(R.string.handwritten_upload_fail);
                }
                break;
        }
    }*/

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }
}