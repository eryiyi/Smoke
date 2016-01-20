package com.xiaolong.Smoke.data;

import com.xiaolong.Smoke.module.News;

import java.util.List;

/**
 * Created by Administrator on 2015/11/1.
 */
public class NewsData extends Data {
    private List<News> data;

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
