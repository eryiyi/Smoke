package com.xiaolong.Smoke.module;

/**
 * Created by Administrator on 2015/11/3.
 */
public class Reply_data {
    private String id;
    private String nTalkId;
    private String sContent;
    private String nHit;
    private String sUserTel;
    private String nIsDel;
    private String nRegisterDate;
    private String nUpdateDate;
    private String sNickName;

    public String getsNickName() {
        return sNickName;
    }

    public void setsNickName(String sNickName) {
        this.sNickName = sNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getnTalkId() {
        return nTalkId;
    }

    public void setnTalkId(String nTalkId) {
        this.nTalkId = nTalkId;
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
