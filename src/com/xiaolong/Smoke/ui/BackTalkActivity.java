package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.Intent;
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
public class BackTalkActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private EditText publish_title;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getExtras().getString("id");
        setContentView(R.layout.back_talk_activity);
        publish_title = (EditText) this.findViewById(R.id.publish_title);
    }

    @Override
    public void onClick(View v) {

    }
    public void add(View view){
        //
        if(StringUtil.isNullOrEmpty(publish_title.getText().toString())){
            Toast.makeText(BackTalkActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(BackTalkActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();

        addBack();

    }
    public void back(View view){
        finish();
    }

    void addBack(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TALK_ISSUE_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(BackTalkActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("back_success");
                                    BackTalkActivity.this.sendBroadcast(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(BackTalkActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(BackTalkActivity.this, R.string.back_fail, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BackTalkActivity.this, R.string.back_fail, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sUserTel", getGson().fromJson(getSp().getString("sUserTel", ""), String.class));
                params.put("nTalkId",id );
                params.put("sContent", publish_title.getText().toString() );
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
