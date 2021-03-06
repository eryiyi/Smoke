package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.WeihaiData;
import com.xiaolong.Smoke.data.WeihaiSingleData;
import com.xiaolong.Smoke.module.Weihai;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/1.
 */
public class WeihaiDetailActivity extends BaseActivity implements View.OnClickListener {
    private Weihai news;
    private TextView content;
    private TextView click_one;
    private TextView title;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weihai_detail_activity);
        news = (Weihai) getIntent().getExtras().get("news");

        content = (TextView) this.findViewById(R.id.content);
        title = (TextView) this.findViewById(R.id.title);
        click_one = (TextView) this.findViewById(R.id.click_one);

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(WeihaiDetailActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getNews();

    }

    @Override
    public void onClick(View v) {

    }
    public void back(View view){
        finish();
    }

    void getNews(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NEWS_HARM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                        click_one.setText(news.getnHit()==null?"0":news.getnHit());
                                        title.setText(Html.fromHtml(news.getsTitle() ==null?"":news.getsTitle()));
                                        content.setText(Html.fromHtml(news.getsContent()==null?"":news.getsContent()));
                                }
                                else {
                                    Toast.makeText(WeihaiDetailActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(WeihaiDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(WeihaiDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id" , news.getId());
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
