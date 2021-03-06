package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
public class RegActivity extends BaseActivity implements View.OnClickListener {
    private EditText email;
    private EditText nickname;
    private Button login_btn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);

        initView();
    }

    private void initView() {
        email = (EditText) this.findViewById(R.id.email);
        nickname = (EditText) this.findViewById(R.id.nickname);
        login_btn = (Button) this.findViewById(R.id.login_btn);

        login_btn.setOnClickListener(this);
    }

    public void back(View view){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login_btn:
                if(StringUtil.isNullOrEmpty(email.getText().toString())){
                    Toast.makeText(RegActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }if(StringUtil.isNullOrEmpty(nickname.getText().toString())){
                    Toast.makeText(RegActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.please_wait).toString();
                progressDialog = new ProgressDialog(RegActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();

                reg();
                break;

        }
    }

    void reg(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    Toast.makeText(RegActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    save("sUserTel" , email.getText().toString());
                                    save("nickname" , nickname.getText().toString());
                                    Intent intent = new Intent("reg_success");
                                    RegActivity.this.sendBroadcast(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(RegActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", email.getText().toString());
                params.put("nick_name", nickname.getText().toString());
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
