package com.project.dailynewsb.dailynews.entity;

/**
 * Created by macbook on 2016/12/8.
 */

public class CommentVo {

    private String uid;// queen","
    private String content;// asdfdf
    private String stamp;//2016-11-19 15:33:23","
    private int cid;//7624,
    private String portrait;//http:\/\/118.244.212.82:9092\/Images\/20161119035003.jpg"

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
