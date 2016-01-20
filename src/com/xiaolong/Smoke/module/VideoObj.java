package com.xiaolong.Smoke.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/3.
 *   "id": "11",
 "title": "康兮寿兮 ·“硒与健康”科普进社区（第一期）",
 "pic": "http://jiankang.wphl.net/./Uploads/2015-07-21/55ad9f35275f2.png",
 "link_url": "http://player.youku.com/embed/XMTI3MzIyOTYxMg",
 "favour_num": "0",
 "dianzan_num": "0",
 "view_num": "0",
 "dateline": "1436318809",
 "is_hot": "1",
 "type_id": "12",
 "zanzhu": "优酷视频",
 "is_tuijian": "0",
 "introduction": "这个是内容的介绍2"
 */
public class VideoObj implements Serializable {
    private String id;
    private String title;
    private String pic;
    private String link_url;
    private String favour_num;
    private String dianzan_num;
    private String view_num;
    private String dateline;
    private String is_hot;
    private String type_id;
    private String zanzhu;
    private String is_tuijian;
    private String introduction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getFavour_num() {
        return favour_num;
    }

    public void setFavour_num(String favour_num) {
        this.favour_num = favour_num;
    }

    public String getDianzan_num() {
        return dianzan_num;
    }

    public void setDianzan_num(String dianzan_num) {
        this.dianzan_num = dianzan_num;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getZanzhu() {
        return zanzhu;
    }

    public void setZanzhu(String zanzhu) {
        this.zanzhu = zanzhu;
    }

    public String getIs_tuijian() {
        return is_tuijian;
    }

    public void setIs_tuijian(String is_tuijian) {
        this.is_tuijian = is_tuijian;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
