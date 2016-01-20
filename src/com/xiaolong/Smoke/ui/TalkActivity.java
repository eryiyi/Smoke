package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.adapter.TalkAdapter;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.TalkData;
import com.xiaolong.Smoke.module.TalkObj;
import com.xiaolong.Smoke.util.StringUtil;
import com.xiaolong.Smoke.widget.MainPopMenu;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/31.
 */
public class TalkActivity extends BaseActivity implements View.OnClickListener,MainPopMenu.OnItemClickListener {
    private ProgressDialog progressDialog;
    private List<TalkObj> listsNews  = new ArrayList<TalkObj>();
    private String cat_id;

    private ListView lstv;
    private TalkAdapter adapter;

    public MainPopMenu mainPopMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_activity);

        initView();

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(TalkActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getNews();

    }

    private void initView() {
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new TalkAdapter(listsNews, TalkActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TalkObj news = listsNews.get(position);
                Intent detailNews = new Intent(TalkActivity.this, TalkDetailActivity.class);
                detailNews.putExtra("news", news);
                startActivity(detailNews);
            }
        });

        mainPopMenu = new MainPopMenu(this);
        mainPopMenu.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View view) {

    }

    public void back(View view){
        finish();
    }

    void getNews(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TALK_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    TalkData data = getGson().fromJson(s, TalkData.class);
                                    if(data != null && data.getData()!= null){
                                        listsNews.addAll(data.getData());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(TalkActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(TalkActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TalkActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    @Override
    public void onItemClick(int index) {
        switch (index) {
            case 0:
                Intent titleView = new Intent(TalkActivity.this, PublishTalkActivity.class);
                startActivity(titleView);
                break;
            case 1:
                Intent minetz = new Intent(TalkActivity.this, MineTzActivity.class);
                startActivity(minetz);
                break;
        }
    }

    //弹出顶部主菜单
    public void onTopMenuPopupButtonClick(View view) {
        mainPopMenu.showAsDropDown(view);
    }

}
