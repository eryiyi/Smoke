package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.adapter.TalkCommentAdapter;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.TalkSingleData;
import com.xiaolong.Smoke.module.Reply_data;
import com.xiaolong.Smoke.module.TalkObj;
import com.xiaolong.Smoke.util.RelativeDateFormat;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/1.
 */
public class TalkDetailActivity extends BaseActivity implements View.OnClickListener {
    private TalkObj news;
    private TextView title;
    private TextView name;
    private TextView dateline;
    private TextView item_title;
    private TextView content;
    private TextView click_one;
    private ProgressDialog progressDialog;
    private ListView lstv;
    private List<Reply_data> listsNews = new ArrayList<>();
    private TalkCommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.talk_detail_activity);
        news = (TalkObj) getIntent().getExtras().get("news");

        title = (TextView) this.findViewById(R.id.title);
        item_title = (TextView) this.findViewById(R.id.item_title);
        dateline = (TextView) this.findViewById(R.id.dateline);
        name = (TextView) this.findViewById(R.id.name);
        content = (TextView) this.findViewById(R.id.content);
        click_one = (TextView) this.findViewById(R.id.click_one);
        this.findViewById(R.id.backtie).setOnClickListener(this);

        click_one.setText(news.getnHit()==null?"0":news.getnHit());
        content.setText(news.getsContent()==null?"":news.getsContent());
        item_title.setText(news.getsTitle()==null?"":news.getsTitle());
        title.setText(news.getsTitle()==null?"":news.getsTitle());
//        dateline.setText(news.getnRegisterDate()==null?"":news.getnRegisterDate());
        dateline.setText(RelativeDateFormat.format(Long.parseLong((news.getnRegisterDate() == null ? "" : news.getnRegisterDate()) + "000")));
        name.setText(news.getsNickName()==null?"":news.getsNickName());

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new TalkCommentAdapter(listsNews, TalkDetailActivity.this);
        lstv.setAdapter(adapter);
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(TalkDetailActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        getNews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backtie:
                //回帖
                Intent backT = new Intent(TalkDetailActivity.this, BackTalkActivity.class);
                backT.putExtra("id",news.getId() );
                startActivity(backT);
                break;
        }

    }
    public void back(View view){
        finish();
    }


    void getNews(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TALK_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    TalkSingleData data = getGson().fromJson(s, TalkSingleData.class);
                                    if(data != null && data.getData()!= null){
                                        listsNews.clear();
                                        listsNews.addAll(data.getData().getReply_data());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(TalkDetailActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(TalkDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(TalkDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", news.getId());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }
    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("back_success")) {
                getNews();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("back_success");
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }
}
