package com.xiaolong.Smoke.module;

import java.util.List;

/**
 * Created by Administrator on 2015/11/1.
 */
public class CatObj {
    private String id;
    private String sCatName;
    private List<CatSon> son;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsCatName() {
        return sCatName;
    }

    public void setsCatName(String sCatName) {
        this.sCatName = sCatName;
    }

    public List<CatSon> getSon() {
        return son;
    }

    public void setSon(List<CatSon> son) {
        this.son = son;
    }
}
