package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.MainActivity;
import com.project.dailynewsb.dailynews.adapter.NewsAdapter;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.entity.NewsTypeVo;
import com.project.dailynewsb.dailynews.util.HttpRequestUtil;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 2016/11/29.
 */

public class NewsFragment extends Fragment {

    private TabLayout news_tl;
    private ViewPager news_vp;
    private ArrayList<NewsTypeVo.NewsTypeVoDataSubgrp> newsTypeVos;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, null);
        news_tl = (TabLayout) view.findViewById(R.id.news_tl);
        news_vp = (ViewPager) view.findViewById(R.id.news_vp);

        //初始化数据
        newsTypeVos = new ArrayList<>();

        // 准备适配器
        mainActivity.showWaitingProgress();

        //获取新闻类型
        new NewsTypeAsyncTask().execute();

        return view;
    }

    /**
     * 获取新闻分类：ver=版本号&imei=手机标识符
     */
    public class NewsTypeAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            //数据请求
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("ver", "0"));
            pairs.add(new BasicNameValuePair("imei", "0"));
            String result = HttpRequestUtil.request(AllRequestUrl.newsType, pairs);

            return result;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            //数据解析
            Log.e("TAL", "result:" + aVoid);
            List<NewsTypeVo> list = HttpResolutionUtil.resolution(aVoid, NewsTypeVo.class, mainActivity, "新闻类型");
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getSubgrp().size(); j++) {
                    newsTypeVos.add(list.get(i).getSubgrp().get(j));
                }
            }

            //更新主界面
            mainActivity.hideWaitingProgress();
            news_vp.setAdapter(new NewsAdapter(getChildFragmentManager(), newsTypeVos));
            news_tl.setupWithViewPager(news_vp);

        }
    }
}