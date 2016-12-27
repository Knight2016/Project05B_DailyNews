package com.project.dailynewsb.dailynews.entity;

import java.util.List;

/**
 * Created by macbook on 2016/11/9.
 */

public class UserVo {

    private String uid;//用户名
    private String portrait;//用户图标
    private String integration;//用户积分票总数
    private String comnum;//评论总数
    private List<UserDataLoginlogVo> loginlog;//登录记录

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public String getComnum() {
        return comnum;
    }

    public void setComnum(String comnum) {
        this.comnum = comnum;
    }

    public List<UserDataLoginlogVo> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<UserDataLoginlogVo> loginlog) {
        this.loginlog = loginlog;
    }

    public class UserDataLoginlogVo {

        private String time;
        private String address;
        private int device;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }
    }
}
