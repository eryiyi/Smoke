package com.xiaolong.Smoke.module;

import com.xiaolong.Smoke.util.StringUtil;

/**
 * Created by Administrator on 2015/8/12.
 */
public class AdSlide {
    private String id;
    private String sImage;
    private String sUrl;
    private String nType;
    private String nIsDel;
    private String nRegisterDate;
    private String nUpdateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getsUrl() {
        return sUrl;
    }

    public void setsUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public String getnType() {
        return nType;
    }

    public void setnType(String nType) {
        this.nType = nType;
    }

    public String getnIsDel() {
        return nIsDel;
    }

    public void setnIsDel(String nIsDel) {
        this.nIsDel = nIsDel;
    }

    public String getnRegisterDate() {
        return nRegisterDate;
    }

    public void setnRegisterDate(String nRegisterDate) {
        this.nRegisterDate = nRegisterDate;
    }

    public String getnUpdateDate() {
        return nUpdateDate;
    }

    public void setnUpdateDate(String nUpdateDate) {
        this.nUpdateDate = nUpdateDate;
    }
}
