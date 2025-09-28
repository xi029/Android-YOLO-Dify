package com.tencent.yolov8ncnn;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.Activity; // 使用 Activity 而不是 AppCompatActivity

public class WebviewActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 初始化 WebView 并加载 URL
        webView = (WebView) findViewById(R.id.webview);

        // 启用 JavaScript 支持
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // 设置 WebView 的额外设置以增强兼容性
        webView.getSettings().setDomStorageEnabled(true);  // 启用 DOM 存储
        webView.getSettings().setAllowFileAccess(true);    // 允许文件访问
        webView.getSettings().setAllowContentAccess(true); // 允许内容访问
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 允许混合内容

        // 加载目标 URL
        webView.loadUrl("https://share.fastgpt.in/chat/share?shareId=71lxicwxausiwq43xix2o0fh");
    }
}