package com.xiaolong.Smoke.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/4.
 * "id": "1",
 "sImage": "",
 "sName": "我是医院",
 "sAddress": "我是地址",
 "sTel": "我是电话",
 "sLng": " ",
 "sLat": " ",
 "nIsDel": "0",
 "nRegisterDate": "0",
 "nUpdateDate": "0"
 */
public class DocObj  implements Serializable{
    private String id;
    private String sImage;
    private String sName;
    private String sAddress;
    private String sTel;
    private String sLng;
    private String sLat;
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

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsTel() {
        return sTel;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel;
    }

    public String getsLng() {
        return sLng;
    }

    public void setsLng(String sLng) {
        this.sLng = sLng;
    }

    public String getsLat() {
        return sLat;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
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
