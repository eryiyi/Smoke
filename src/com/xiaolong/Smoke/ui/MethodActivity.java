package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.adapter.MethodAdapter;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.MethodsData;
import com.xiaolong.Smoke.module.MethodObj;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/31.
 */
public class MethodActivity extends BaseActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private List<MethodObj> listsNews  = new ArrayList<MethodObj>();
//    private String cat_id;

    private ListView lstv;
    private MethodAdapter adapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.method_activity);
//        cat_id = getIntent().getExtras().getString("cat_id");

        initView();

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(MethodActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getNews();

    }

    private void initView() {
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new MethodAdapter(listsNews, MethodActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MethodObj news = listsNews.get(position);
                Intent detailNews = new Intent(MethodActivity.this, MethodsDetailActivity.class);
                detailNews.putExtra("news", news);
                startActivity(detailNews);
            }
        });
        title = (TextView) this.findViewById(R.id.title);

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
                InternetURL.GET_NEWS_METHOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    MethodsData data = getGson().fromJson(s, MethodsData.class);
                                    if(data != null && data.getData()!= null){
                                        listsNews.addAll(data.getData());
                                        title.setText(listsNews.size() + "个方法你用了几个？");
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(MethodActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MethodActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MethodActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("id" , cat_id);
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
}
