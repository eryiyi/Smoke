package com.xiaolong.Smoke.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by liuzwei on 2015/1/6.
 */
public class Utils {
    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static final String DAY_NIGHT_MODE="daynightmode";
    public static final String DEVIATION="deviationrecalculation";
    public static final String JAM="jamrecalculation";
    public static final String TRAFFIC="trafficbroadcast";
    public static final String CAMERA="camerabroadcast";
    public static final String SCREEN="screenon";
    public static final String THEME="theme";
    public static final String ISEMULATOR="isemulator";


    public static final String ACTIVITYINDEX="activityindex";

    public static final int SIMPLEHUDNAVIE=0;
    public static final int EMULATORNAVI=1;
    public static final int SIMPLEGPSNAVI=2;
    public static final int SIMPLEROUTENAVI=3;


    public static final boolean DAY_MODE=false;
    public static final boolean NIGHT_MODE=true;
    public static final boolean YES_MODE=true;
    public static final boolean NO_MODE=false;
    public static final boolean OPEN_MODE=true;
    public static final boolean CLOSE_MODE=false;


}
