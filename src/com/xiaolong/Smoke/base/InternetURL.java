package com.xiaolong.Smoke.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://jieyan.apptech.space/mobile.php/";
    public static final String INTERNAL_PIC = "http://jieyan.apptech.space";
    //接口访问密钥获取（ user.api- - authkey
    public static final String GET_TOKEN = INTERNAL + "user.api-authkey";
    //2 2 、 注册 （ user.api- - sendCode/verityCode/register）
//    public static final String GET_CODE = INTERNAL + "user.api-sendCode";
//    public static final String GET_VERTY_CODE = INTERNAL + "user.api-verityCode";
//mail nick_name
    public static final String GET_REGISTER =  "http://jieyan.apptech.space/mail/send";
    //3 3 、登陆 (user.api- -  mail
    public static final String GET_LOGIN = INTERNAL + "user.api-login";
    //4 4 、忘记密码 (user.api- -
//    public static final String GET_FORGET_PWR = INTERNAL + "user.api-forgetPassword";
    //、修改密码 (user.api- -
//    public static final String GET_UPDATE_PWR = INTERNAL + "user.api-updatePassword";

    //、新闻分类（ news .api- -
    public static final String GET_CAT= INTERNAL + "news.api-cat";
    //  3 3 、新闻列表（ news .api- - index
    public static final String GET_NEWS= INTERNAL + "news.api-index";
    //新闻详细（ news .api- - detail
    public static final String GET_NEWS_DETAIL = INTERNAL + "news.api-detail";
    //戒烟方法 （ news .api- -
    public static final String GET_NEWS_METHOD = INTERNAL + "news.api-method";
    //吸烟危害（ news .api- - harm
    public static final String GET_NEWS_HARM = INTERNAL + "news.api-harm";
    //视频列表（ video .api- - lists
    public static final String GET_NEWS_VIDEO_LIST = INTERNAL + "video.api-lists";
    //医院列表（ hospital .api- - lists
    public static final String GET_NEWS_HOSPITAL = INTERNAL + "hospital.api-lists";
    // 、 论坛 列表 （ tal lk k .api- - lists
    public static final String GET_TALK_LIST = INTERNAL + "talk.api-lists";
    // 、 发布 （ tal lk k .api- - issue
    public static final String GET_TALK_ISSUE= INTERNAL + "talk.api-issue";
//    、、 、 回帖 （ tal lk k .api- - rep pl ly y
    public static final String GET_TALK_ISSUE_REPLY= INTERNAL + "talk.api-reply";
    // 、 详细 （ tal lk k .api- - detai il l
    public static final String GET_TALK_DETAIL= INTERNAL + "talk.api-detail";
    // 、 删帖 （ talk .api- - del
    public static final String GET_TALK_DELETE= INTERNAL + "talk.api-del";
    //轮播
    public static final String GET_ADV= INTERNAL + "adv.api-lists";

}
