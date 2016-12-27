package com.project.dailynewsb.dailynews.config;

/**
 * Created by macbook on 2016/12/1.
 */

public class AllRequestUrl {

    //根路径（调试环境→测试环境→生产环境）
    public static String rootPath = "http://118.244.212.82:9092/newsClient/";

    //新闻分类
    public static String newsType = rootPath + "news_sort";

    //新闻列表
    public static String newsList = rootPath + "news_list";

    //获取评论数量
    public static String conmentNum = rootPath + "cmt_num";

    //获取评论列表
    public static String conmentList = rootPath + "cmt_list";

    //注册接口
    public static String register = rootPath + "user_register";

    //登录接口
    public static String login = rootPath + "user_login";

    //个人中心接口
    public static String personCenter = rootPath + "user_home";

    //个人中心接口
    public static String sendComment = rootPath + "cmt_commit";

    //头像上传
    public static String uploadImage = rootPath + "user_image";

    //版本更新
    public static String update = rootPath + "update";


}
