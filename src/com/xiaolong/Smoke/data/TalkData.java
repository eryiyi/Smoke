package com.xiaolong.Smoke.data;

import com.xiaolong.Smoke.module.TalkObj;

import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class TalkData extends Data {
    private List<TalkObj> data;

    public List<TalkObj> getData() {
        return data;
    }

    public void setData(List<TalkObj> data) {
        this.data = data;
    }
}
