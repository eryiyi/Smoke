package com.xiaolong.Smoke.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.MainActivity;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.LoginObjData;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/4.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText email;
    private Button login_btn;
    private TextView reg;
    private TextView forget;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.login_activity);

        initView();
        if(email != null && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sUserTel", ""), String.class))){
            email.setText(getGson().fromJson(getSp().getString("sUserTel", ""), String.class));
        }

    }

    private void initView() {
        email = (EditText) this.findViewById(R.id.email);
        login_btn = (Button) this.findViewById(R.id.login_btn);
        reg = (TextView) this.findViewById(R.id.reg);
        forget = (TextView) this.findViewById(R.id.forget);

        login_btn.setOnClickListener(this);
        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg:
                Intent regV = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(regV);
                break;
            case R.id.login_btn:
                if(StringUtil.isNullOrEmpty(email.getText().toString())){
                    Toast.makeText(LoginActivity.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.please_wait).toString();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();

                login();
                break;
            case R.id.forget:
                Intent forgetV = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(forgetV);
                break;
        }
    }

    void login(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    LoginObjData data = getGson().fromJson(s, LoginObjData.class);
//                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                    if(data != null && data.getData() != null){
                                        save("sUserTel", email.getText().toString());
                                        save("id", data.getData().getId());
                                        save("mail", data.getData().getMail());
                                        save("validate", data.getData().getValidate());
                                        save("active", data.getData().getActive());
                                        save("reg_time", data.getData().getReg_time());
                                        save("up_time", data.getData().getUp_time());
                                        save("nick_name", data.getData().getNick_name());
                                    }
                                    Intent mainV = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainV);
                                    finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", email.getText().toString());
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
            if (action.equals("reg_success")) {
                email.setText(getGson().fromJson(getSp().getString("sUserTel", ""), String.class));
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("reg_success");
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }


}
