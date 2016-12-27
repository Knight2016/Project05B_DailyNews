package com.project.dailynewsb.dailynews.entity;

import java.io.Serializable;

/**
 * Created by macbook on 2016/12/1.
 */

public class NewsVo implements Serializable {

    private String summary;//全面深化改革是一项复杂的系统工程，如何认识好深改所涉及的各种关系，事关改革成败。
    private String icon;//http:\/\/118.244.212.82:9092\/Images\/20160926120752.jpg","
    private String stamp;//2016-09-26 11:31:20.0","
    private String title;//习近平的改革认识论","
    private int nid;//28
    private String link;//http:\/\/china.huanqiu.com\/article\/2016-09\/9484876.html?from=bdwz","
    private int type;//1

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
