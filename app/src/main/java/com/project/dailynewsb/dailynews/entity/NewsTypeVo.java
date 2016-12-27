package com.project.dailynewsb.dailynews.entity;

import java.util.List;

/**
 * Created by macbook on 2016/12/6.
 */

public class NewsTypeVo {


    private int gid;
    private String group;// "新闻"
    private List<NewsTypeVoDataSubgrp> subgrp;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<NewsTypeVoDataSubgrp> getSubgrp() {
        return subgrp;
    }

    public void setSubgrp(List<NewsTypeVoDataSubgrp> subgrp) {
        this.subgrp = subgrp;
    }

    public class NewsTypeVoDataSubgrp {

        private String subgroup;//"社会"
        private int subid;// 2

        public int getSubid() {
            return subid;
        }

        public void setSubid(int subid) {
            this.subid = subid;
        }

        public String getSubgroup() {
            return subgroup;
        }

        public void setSubgroup(String subgroup) {
            this.subgroup = subgroup;
        }
    }
}
