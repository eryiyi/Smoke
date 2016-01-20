package com.xiaolong.Smoke.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.base.BaseActivity;

/**
 * Created by Administrator on 2015/11/5.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private String url;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getExtras().getString("url");
        setContentView(R.layout.webview_activity);
        webView = (WebView) this.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        // 加载URL
        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {

    }

    public void back(View view){
        finish();
    }

}
