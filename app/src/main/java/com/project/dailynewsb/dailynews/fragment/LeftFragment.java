package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.MainActivity;

/**
 * 左侧菜单栏Fragment
 * Created by macbook on 2016/11/29.
 */

public class LeftFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rl_news, rl_reading, rl_local, rl_comment, rl_photo;

    private MainActivity mainActivity;

    //依附
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_left, null);
        rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);
        rl_reading = (RelativeLayout) view.findViewById(R.id.rl_reading);
        rl_local = (RelativeLayout) view.findViewById(R.id.rl_local);
        rl_comment = (RelativeLayout) view.findViewById(R.id.rl_comment);
        rl_photo = (RelativeLayout) view.findViewById(R.id.rl_photo);

        //设置监听
        rl_news.setOnClickListener(this);
        rl_reading.setOnClickListener(this);
        rl_local.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
        rl_photo.setOnClickListener(this);

        //获取数据（默认展示新闻）
        if (mainActivity.flag == 0) {
            rl_news.setSelected(true);
            mainActivity.changeFragment(1, "新闻");
        } else if (mainActivity.flag == 6) {
            mainActivity.changeFragment(6, "登录");
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        // # 1 展示主界面♥

        // # 2 更改标题头♥

        // # 3 替换Fragment

        // # 4 展示选中效果♥

        switch (v.getId()) {

            case R.id.rl_news:
                //新闻
                rl_news.setSelected(true);
                rl_reading.setSelected(false);
                rl_local.setSelected(false);
                rl_comment.setSelected(false);
                rl_photo.setSelected(false);

                //更换主界面Fragment
                mainActivity.changeFragment(1, "新闻");

                break;

            case R.id.rl_reading:
                //收藏
                rl_reading.setSelected(true);
                rl_news.setSelected(false);
                rl_local.setSelected(false);
                rl_comment.setSelected(false);
                rl_photo.setSelected(false);

                mainActivity.changeFragment(2, "收藏");

                break;

            case R.id.rl_local:
                //本地
                rl_local.setSelected(true);
                rl_news.setSelected(false);
                rl_reading.setSelected(false);
                rl_comment.setSelected(false);
                rl_photo.setSelected(false);

                mainActivity.changeFragment(3, "本地");

                break;

            case R.id.rl_comment:
                //跟帖
                rl_comment.setSelected(true);
                rl_news.setSelected(false);
                rl_reading.setSelected(false);
                rl_local.setSelected(false);
                rl_photo.setSelected(false);

                mainActivity.changeFragment(4, "跟帖");


                break;

            case R.id.rl_photo:
                //图片
                rl_photo.setSelected(true);
                rl_news.setSelected(false);
                rl_reading.setSelected(false);
                rl_local.setSelected(false);
                rl_comment.setSelected(false);

                mainActivity.changeFragment(5, "图片");

                break;

        }
    }
}
