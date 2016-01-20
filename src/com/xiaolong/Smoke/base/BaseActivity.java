package com.xiaolong.Smoke.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.xiaolong.Smoke.SmokeApplication;
import com.xiaolong.Smoke.upload.MultiPartStringRequest;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends FragmentActivity  {
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;
    /**
     * 屏幕的宽度和高度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /**
         * 获取屏幕宽度和高度
         */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        ActivityTack.getInstanse().addActivity(this);
    }

    /**
     * 获取当前Application
     *
     * @return
     */
    public SmokeApplication getMyApp() {
        return (SmokeApplication) getApplication();
    }

    //存储sharepreference
    public void save(String key, Object value) {
        getMyApp().getSp().edit()
                .putString(key, getMyApp().getGson().toJson(value))
                .commit();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 获取Volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        return getMyApp().getRequestQueue();
    }

    /**
     * 获取Gson
     *
     * @return
     */
    public Gson getGson() {
        return getMyApp().getGson();
    }

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    public SharedPreferences getSp() {
        return getMyApp().getSp();
    }

    /**
     * 获取自定义线程池
     *
     * @return
     */
    public ExecutorService getLxThread() {
        return getMyApp().getLxThread();
    }



    public void addPutUploadFileRequest(final String url,
                                        final Map<String, File> files, final Map<String, String> params,
                                        final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                        final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };

        getRequestQueue().add(multiPartRequest);
    }



//    public boolean handleMessage(Message msg) {
//        if (pd != null && pd.isShowing()) {
//            pd.dismiss();
//        }
//        int event = msg.arg1;
//        int result = msg.arg2;
//        Object data = msg.obj;
//        if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//
//        } else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
//
//        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//            //校验手机号
//            try {
//                HashMap<String, String> mapmobile = (HashMap<String, String>) msg.obj;
//                mobileReg = mapmobile.get("phone");
//                //验证成功
//                if (!StringUtil.isNullOrEmpty(mobileReg)) {
//                    if (typeCard == 0) {
//                        //注册
//                        Intent next = new Intent(this, RegistTwoActivity.class);
//                        next.putExtra(Constants.REGISTER_SCHOOLID, RegistSelectSchoolActivity.schoolId);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 1) {
//                        //找回密码
//                        Intent next = new Intent(this, FindPwrOneActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                    if (typeCard == 2) {
//                        //重置手机号
//                        Intent next = new Intent(this, ReSetMobileActivity.class);
//                        next.putExtra(Constants.REGISTER_MOBILE, mobileReg);
//                        startActivity(next);
//                    }
//                }
//            } catch (Exception e) {
//                mobileReg = "";
//            }
//        } else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
//        }
//        return false;
//    }

    protected void onDestroy() {
        ActivityTack.getInstanse().removeActivity(this);
        super.onDestroy();
    }
}
