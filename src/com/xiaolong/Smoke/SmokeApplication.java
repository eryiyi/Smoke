package com.xiaolong.Smoke;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: ${zhanghailong}
 * Date: 2015/1/29
 * Time: 17:04
 * 类的功能、说明写在此处.
 */
public class SmokeApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private ExecutorService lxThread;
    private Gson gson;
    private RequestQueue requestQueue;
    private SharedPreferences sp;

    private static SmokeApplication application;

    public static  Double lat;
    public static  Double lon;
    public static  String desc;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        requestQueue = Volley.newRequestQueue(this);
        gson = new Gson();
        lxThread = Executors.newFixedThreadPool(20);
        sp = getSharedPreferences("smoke_manage", Context.MODE_PRIVATE);
        imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, new BitmapCache());
        initImageLoader(this);
    }


    public static Context getContext() {
        return application;
    }


    /**
     * 获取自定义线程池
     *
     * @return
     */
    public ExecutorService getLxThread() {
        if (lxThread == null) {
            lxThread = Executors.newFixedThreadPool(20);
        }
        return lxThread;
    }

    /**
     * 获取Gson
     *
     * @return
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * 获取Volley请求队列
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    public SharedPreferences getSp() {
        if (sp == null) {
            sp = getSharedPreferences("smoke_manage", Context.MODE_PRIVATE);
        }
        return sp;
    }



    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//头像图片
    public static DisplayImageOptions tpOptions;//详情页图片
    public static DisplayImageOptions newsOptions;//详情页图片
    public static DisplayImageOptions methodsOptions;//详情页图片

    public SmokeApplication() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.photo_failed)
                .showImageForEmptyUri(R.drawable.photo_failed)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.photo_failed)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();                                       // 创建配置过得DisplayImageOption对象
        txOptions = new DisplayImageOptions.Builder()//头像
                .showImageOnLoading(R.drawable.main_top_img)
                .showImageForEmptyUri(R.drawable.main_top_img)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.main_top_img)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型头像
                .build();
        tpOptions = new DisplayImageOptions.Builder()//图片
                .showImageOnLoading(R.drawable.photo_failed)
                .showImageForEmptyUri(R.drawable.photo_failed)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.photo_failed)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型图片
                .build();
        newsOptions = new DisplayImageOptions.Builder()//图片
                .showImageOnLoading(R.drawable.four_item_head)
                .showImageForEmptyUri(R.drawable.four_item_head)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.four_item_head)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型图片
                .build();
        methodsOptions = new DisplayImageOptions.Builder()//图片
                .showImageOnLoading(R.drawable.two_book)
                .showImageForEmptyUri(R.drawable.two_book)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.two_book)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型图片
                .build();
    }

    /**
     * 初始化图片加载组件ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private com.android.volley.toolbox.ImageLoader imageLoader;

    private class BitmapCache implements com.android.volley.toolbox.ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

    public com.android.volley.toolbox.ImageLoader getImageLoader() {
        return imageLoader;
    }


    private static SmokeApplication instance;

    // 构造方法
    // 实例化一次
    public synchronized static SmokeApplication getInstance() {
        if (null == instance) {
            instance = new SmokeApplication();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // 关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}

