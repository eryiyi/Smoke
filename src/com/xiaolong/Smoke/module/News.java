package com.xiaolong.Smoke.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/1.
 */
public class News  implements Serializable{
    private String id;
    private String sImage;
    private String sTitle;
    private String sContent;
    private String nHit;
    private String sUserTel;
    private String nCatId;
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

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }

    public String getsContent() {
        return sContent;
    }

    public void setsContent(String sContent) {
        this.sContent = sContent;
    }

    public String getnHit() {
        return nHit;
    }

    public void setnHit(String nHit) {
        this.nHit = nHit;
    }

    public String getsUserTel() {
        return sUserTel;
    }

    public void setsUserTel(String sUserTel) {
        this.sUserTel = sUserTel;
    }

    public String getnCatId() {
        return nCatId;
    }

    public void setnCatId(String nCatId) {
        this.nCatId = nCatId;
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
