package com.xiaolong.Smoke;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaolong.Smoke.adapter.OnClickContentItemListener;
import com.xiaolong.Smoke.adapter.ViewPagerAdapter;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.base.InternetURL;
import com.xiaolong.Smoke.data.AccessTokenData;
import com.xiaolong.Smoke.data.AdSlideData;
import com.xiaolong.Smoke.data.CatObjData;
import com.xiaolong.Smoke.module.AdSlide;
import com.xiaolong.Smoke.module.CatObj;
import com.xiaolong.Smoke.module.CatSon;
import com.xiaolong.Smoke.ui.*;
import com.xiaolong.Smoke.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends BaseActivity  implements View.OnClickListener,OnClickContentItemListener,AMapLocationListener {
    private LocationManagerProxy mLocationManagerProxy;
    Resources res;
    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<AdSlide> lists = new ArrayList<AdSlide>();

    private LinearLayout liner_one;
    private LinearLayout liner_two;
    private LinearLayout liner_six;
    private ProgressDialog progressDialog;

    private List<CatObj> catObjs = new ArrayList<CatObj>();//分类
    private List<CatSon> catSons = new ArrayList<CatSon>();//小分类

    String reg_time = "";
    private TextView number_day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        reg_time = getGson().fromJson(getSp().getString("reg_time", ""), String.class);
        if(!StringUtil.isNullOrEmpty(reg_time)){
//            reg_time = RelativeDateFormat.format(Long.parseLong((reg_time) + "000"));
            Date date = new Date(Long.valueOf(reg_time + "000"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            reg_time = format.format(date);
        }
        res = getResources();
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getCat();
        getLunbo();
        init();
    }

    private void initView() throws ParseException {
        liner_two = (LinearLayout) this.findViewById(R.id.liner_two);
        liner_one = (LinearLayout) this.findViewById(R.id.liner_one);
        liner_six = (LinearLayout) this.findViewById(R.id.liner_six);
        liner_one.setOnClickListener(this);
        liner_two.setOnClickListener(this);
        liner_six.setOnClickListener(this);
        this.findViewById(R.id.liner_three).setOnClickListener(this);
        this.findViewById(R.id.liner_five).setOnClickListener(this);
        this.findViewById(R.id.liner_four).setOnClickListener(this);
        number_day = (TextView) this.findViewById(R.id.number_day);
        if(!StringUtil.isNullOrEmpty(reg_time))
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = new Date();
            //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
            long to = df.parse(df.format(dt)).getTime();
            long from = df.parse(reg_time).getTime();
            number_day.setText(String.valueOf((to - from) / (1000 * 60 * 60 * 24)));
        }
    }

    private void initViewPager() {

        adapter = new ViewPagerAdapter(MainActivity.this);
        adapter.change(lists);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) this.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) this.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(MainActivity.this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapter.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
//        if (!StringUtil.isNullOrEmpty(lists.get(position).getNewsTitle())){
//            titleSlide = lists.get(position).getNewsTitle();
//            if(titleSlide.length() > 13){
//                titleSlide = titleSlide.substring(0,12);
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }else{
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }
//        }

    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                AdSlide adSlide = lists.get(position);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", adSlide.getsUrl());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.liner_two:
            {
                //方法
                Intent methodView = new Intent(MainActivity.this, MethodActivity.class);
//                String cat_id= "";
//                if(catObjs != null){
//                    for(CatObj catObj : catObjs){
//                        if("健康科普".equals(catObj.getsCatName())){
//                            List<CatSon>  listSon =  catObj.getSon();
//                            if(listSon != null && listSon.size()>0){
//                                for(CatSon catSon:listSon){
//                                    if("劝阻技巧".equals(catSon.getsCatName())){
//                                        cat_id = catSon.getId();
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                    }
//                }
//                methodView.putExtra("cat_id", cat_id);
                startActivity(methodView);
            }
                break;
            case R.id.liner_six:
            {
                //危害
                Intent methodView = new Intent(MainActivity.this, WeihaiActivity.class);
//                String cat_id= "";
//                if(catObjs != null){
//                    for(CatObj catObj : catObjs){
//                        if("健康科普".equals(catObj.getsCatName())){
//                            List<CatSon>  listSon =  catObj.getSon();
//                            if(listSon != null && listSon.size()>0){
//                                for(CatSon catSon:listSon){
//                                    if("烟草危害".equals(catSon.getsCatName())){
//                                        cat_id = catSon.getId();
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                    }
//                }
//                methodView.putExtra("cat_id", cat_id);
                startActivity(methodView);
            }
                break;
            case R.id.liner_one:
            {
                Intent methodView = new Intent(MainActivity.this, NewsActivity.class);
                String cat_id= "";
                if(catObjs != null){
                    for(CatObj catObj : catObjs){
                        if("新闻动态".equals(catObj.getsCatName())){
                            cat_id = catObj.getId();
                            break;
                        }
                    }
                }
                methodView.putExtra("cat_id", cat_id);
                startActivity(methodView);
            }
                break;
            case R.id.liner_three:
            {
                Intent videoView = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(videoView);
            }
            break;
            case R.id.liner_four:
            {
                Intent videoView = new Intent(MainActivity.this, HospitalActivity.class);
                startActivity(videoView);
            }
            break;
            case R.id.liner_five:
            {
                Intent videoView = new Intent(MainActivity.this, TalkActivity.class);
                startActivity(videoView);
            }
            break;
        }
    }


    void loginData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    AccessTokenData data = getGson().fromJson(s, AccessTokenData.class);
                                }
                                else {
                                    Toast.makeText(MainActivity.this,  jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.login_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, R.string.login_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", "");
                params.put("password", "");
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


    void getCat(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_CAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    CatObjData data = getGson().fromJson(s, CatObjData.class);
                                    if(data != null && data.getData()!= null){
                                        catObjs.addAll(data.getData());
                                    }
                                    if(catObjs != null && catObjs.size()>0){
                                        for(CatObj catObj: catObjs){
                                            catSons.addAll(catObj.getSon());
                                        }
                                    }

                                }
                                else {
                                    Toast.makeText(MainActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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


    void getLunbo(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_ADV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    AdSlideData data = getGson().fromJson(s, AdSlideData.class);
                                    if(data != null && data.getData()!= null){
                                        lists.addAll(data.getData());
                                        initViewPager();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id" , "0");
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


    /**
     * 初始化定位
     */
    private void init() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);

    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            // 定位成功回调信息，设置相关消息
            SmokeApplication.lat = amapLocation.getLatitude();
            SmokeApplication.lon = amapLocation.getLongitude();
            SmokeApplication.desc = amapLocation.getAddress();
//            mLocationLatlngTextView.setText(amapLocation.getLatitude() + "  "
//                    + amapLocation.getLongitude());
//            mLocationAccurancyTextView.setText(String.valueOf(amapLocation
//                    .getAccuracy()));
//            mLocationMethodTextView.setText(amapLocation.getProvider());
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date(amapLocation.getTime());
//
//            mLocationTimeTextView.setText(df.format(date));
//            mLocationDesTextView.setText(amapLocation.getAddress());
//            mLocationCountryTextView.setText(amapLocation.getCountry());
//            if (amapLocation.getProvince() == null) {
//                mLocationProvinceTextView.setText("null");
//            } else {
//                mLocationProvinceTextView.setText(amapLocation.getProvince());
//            }
//            mLocationCityTextView.setText(amapLocation.getCity());
//            mLocationCountyTextView.setText(amapLocation.getDistrict());
//            mLocationRoadTextView.setText(amapLocation.getRoad());
//            mLocationPOITextView.setText(amapLocation.getPoiName());
//            mLocationCityCodeTextView.setText(amapLocation.getCityCode());
//            mLocationAreaCodeTextView.setText(amapLocation.getAdCode());
        } else {
            Log.e("AmapErr", "Location ERR:" + amapLocation.getAMapException().getErrorCode());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }

    protected void onDestroy() {
        super.onDestroy();

    }




}
