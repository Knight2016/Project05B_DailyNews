package com.project.dailynewsb.dailynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.WebViewActivity;
import com.project.dailynewsb.dailynews.adapter.CollectionAdapter;
import com.project.dailynewsb.dailynews.db.DBUtil;
import com.project.dailynewsb.dailynews.entity.NewsVo;

import java.util.ArrayList;

/**
 * 收藏
 * Created by macbook on 2016/11/29.
 */

public class CollectionFragment extends Fragment {

    private ListView collection_lv;
    private ArrayList<NewsVo> newsVos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection, null);
        collection_lv = (ListView) view.findViewById(R.id.collection_lv);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据
        DBUtil dbUtil = new DBUtil(getContext());
        newsVos = new ArrayList<>();
        newsVos.addAll(dbUtil.queryAllFav());
        collection_lv.setAdapter(new CollectionAdapter(newsVos));

        //设置监听
        collection_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //跳转详情页面
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("VO", newsVos.get(position));
                startActivity(intent);

            }
        });
    }
}
