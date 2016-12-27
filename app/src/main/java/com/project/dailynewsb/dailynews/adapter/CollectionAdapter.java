package com.project.dailynewsb.dailynews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.base.MyBaseAdapter;
import com.project.dailynewsb.dailynews.entity.NewsVo;
import com.project.dailynewsb.dailynews.util.ImageLoadUtil;

import java.util.ArrayList;

import static com.project.dailynewsb.dailynews.R.id.news_content;
import static com.project.dailynewsb.dailynews.R.id.news_time;
import static com.project.dailynewsb.dailynews.R.id.news_title;

/**
 * Created by macbook on 2016/12/7.
 */

public class CollectionAdapter extends MyBaseAdapter<NewsVo> {

    private ArrayList<NewsVo> newsVos;

    public CollectionAdapter(ArrayList<NewsVo> newsVos) {
        super(newsVos);
        this.newsVos = newsVos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_sub_item, null);
            viewHolder = new ViewHolder();
            viewHolder.news_icon = (ImageView) convertView.findViewById(R.id.news_icon);
            viewHolder.news_title = (TextView) convertView.findViewById(news_title);
            viewHolder.news_content = (TextView) convertView.findViewById(news_content);
            viewHolder.news_time = (TextView) convertView.findViewById(news_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //直接赋值
        viewHolder.news_title.setText(newsVos.get(position).getTitle());
        viewHolder.news_content.setText(newsVos.get(position).getSummary());
        viewHolder.news_time.setText(newsVos.get(position).getStamp());

        //获取新闻图标（预览图），本身是个链接，javaForGet请求网络端，获取流，通过流转化成Bitmap
        ImageLoadUtil imageLoadUtil = new ImageLoadUtil();
        imageLoadUtil.getImageBitmap(newsVos.get(position).getIcon(), viewHolder.news_icon);

        return convertView;
    }

    public class ViewHolder {

        ImageView news_icon;
        TextView news_title, news_content, news_time;

    }
}
