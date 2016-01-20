package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/4.
 */
public class PublishTalkActivity extends BaseActivity implements View.OnClickListener {
    private EditText publish_title;
    private EditText publish_content;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_talk_activity);

        publish_title = (EditText) this.findViewById(R.id.publish_title);
        publish_content = (EditText) this.findViewById(R.id.publish_content);
    }

    @Override
    public void onClick(View v) {

    }
    public void back(View view){
        finish();
    }
    public void add(View view){
        //
        if(StringUtil.isNullOrEmpty(publish_title.getText().toString())){
            Toast.makeText(PublishTalkActivity.this, "请输入标题", Toast.LENGTH_LONG).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(publish_content.getText().toString())){
            Toast.makeText(PublishTalkActivity.this, "请输入内容", Toast.LENGTH_LONG).show();
            return;
        }


        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(PublishTalkActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        addTalk();
    }

    void addTalk(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TALK_ISSUE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(PublishTalkActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(PublishTalkActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PublishTalkActivity.this, R.string.add_fail, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PublishTalkActivity.this, R.string.add_fail, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sUserTel", getGson().fromJson(getSp().getString("sUserTel", ""), String.class));
                params.put("sTitle", publish_title.getText().toString() );
                params.put("sContent", publish_content.getText().toString() );
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
