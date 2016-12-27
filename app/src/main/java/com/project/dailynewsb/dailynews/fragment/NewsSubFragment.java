package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.MainActivity;
import com.project.dailynewsb.dailynews.adapter.NewsSubAdapter;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.entity.NewsVo;
import com.project.dailynewsb.dailynews.util.HttpRequestUtil;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 列表Fragment
 * Created by macbook on 2016/12/1.
 */

public class NewsSubFragment extends Fragment implements XRecyclerView.LoadingListener {

    private XRecyclerView news_sub_xrv;
    private int newsId;
    private ArrayList<NewsVo> newsVoList;
    private int lastNewsId = 0;//上次请求的新闻ID
    private NewsSubAdapter adapter;
    private MainActivity mainActivity;

    public NewsSubFragment(int newsId) {
        this.newsId = newsId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_sub, null);
        news_sub_xrv = (XRecyclerView) view.findViewById(R.id.news_sub_xrv);

        //初始化数据
        newsVoList = new ArrayList<>();
        adapter = new NewsSubAdapter(newsVoList, mainActivity);
        news_sub_xrv.setAdapter(adapter);

        //初始化XRecyclerView
        initXRecyclerView();

        //获取数据
        new NewsListAsyncTask().execute(true);

        return view;
    }

    /**
     * 获取新闻列表:ver=版本号&subid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20
     */
    public class NewsListAsyncTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开启等待层
            mainActivity.showWaitingProgress();
        }

        @Override
        protected String doInBackground(Boolean... params) {

            if (params[0]) {
                newsVoList.clear();//如果刷新数据清空
            }

            //数据请求
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("ver", "0"));
            pairs.add(new BasicNameValuePair("subid", String.valueOf(newsId)));//对应的subId是谁，列表就会展示谁
            if (params[0]) {
                pairs.add(new BasicNameValuePair("dir", "1"));
                pairs.add(new BasicNameValuePair("nid", "0"));//可以省略
            } else {
                pairs.add(new BasicNameValuePair("dir", "2"));
                pairs.add(new BasicNameValuePair("nid", String.valueOf(lastNewsId)));//可以省略
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = simpleDateFormat.format(new Date());
            pairs.add(new BasicNameValuePair("stamp", date));//当前时间
            pairs.add(new BasicNameValuePair("cnt", "20"));
            String result = HttpRequestUtil.request(AllRequestUrl.newsList, pairs);
            Log.e("TAL", "列表请求:" + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //数据解析
            newsVoList.addAll(HttpResolutionUtil.resolution(s, NewsVo.class, getActivity(), "新闻列表"));
            lastNewsId = newsVoList.get(newsVoList.size() - 1).getNid();

            //界面更新
            mainActivity.hideWaitingProgress();
            news_sub_xrv.refreshComplete();
            news_sub_xrv.loadMoreComplete();
            news_sub_xrv.setLoadingMoreEnabled(true);
            news_sub_xrv.setPullRefreshEnabled(true);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化initXRecyclerView
     */
    public void initXRecyclerView() {

        // # 1 设置方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        news_sub_xrv.setLayoutManager(layoutManager);

        // # 2 设置可上拉下拉
        news_sub_xrv.setPullRefreshEnabled(true);
        news_sub_xrv.setLoadingMoreEnabled(true);

        // # 3 设置上拉下拉动画
        news_sub_xrv.setRefreshProgressStyle(ProgressStyle.BallPulse);
        news_sub_xrv.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        // # 4 设置上拉下拉监听
        news_sub_xrv.setLoadingListener(this);

        // # 5 这是下拉箭头
        //news_sub_xrv.setArrowImageView(R.drawable.btn_back);

        // # 6 添加头文件
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_test, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        //news_sub_xrv.addHeaderView(view);

    }

    @Override
    public void onRefresh() {

        Log.e("TAL", "下拉刷新");
        //数据请求
        new NewsListAsyncTask().execute(true);
        news_sub_xrv.setLoadingMoreEnabled(false);
        news_sub_xrv.setPullRefreshEnabled(false);
    }

    @Override
    public void onLoadMore() {

        Log.e("TAL", "上拉加载更多");
        new NewsListAsyncTask().execute(false);
        news_sub_xrv.setLoadingMoreEnabled(false);
        news_sub_xrv.setPullRefreshEnabled(false);
    }
}
