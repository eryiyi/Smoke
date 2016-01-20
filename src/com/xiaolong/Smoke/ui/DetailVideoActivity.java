package com.xiaolong.Smoke.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.module.VideoObj;

/**
 * Created by Administrator on 2015/11/4.
 */
public class DetailVideoActivity extends BaseActivity implements View.OnClickListener {
    VideoObj  videoObj;
    private WebView webView;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoObj = (VideoObj) getIntent().getExtras().get("news");
        setContentView(R.layout.video_detail_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        title = (TextView) this.findViewById(R.id.title);

        title.setText(videoObj.getTitle()==null?"视频新闻":videoObj.getTitle());

        webView = (WebView) this.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        // 加载URL
        webView.loadUrl(videoObj.getLink_url());
    }

    public void back(View view){
        finish();
    }


    @Override
    public void onClick(View v) {

    }
}
