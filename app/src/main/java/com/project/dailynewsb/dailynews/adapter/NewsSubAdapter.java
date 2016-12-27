package com.project.dailynewsb.dailynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.WebViewActivity;
import com.project.dailynewsb.dailynews.entity.NewsVo;
import com.project.dailynewsb.dailynews.util.ImageLoadUtil;

import java.util.ArrayList;

/**
 * Created by macbook on 2016/12/2.
 */

public class NewsSubAdapter extends XRecyclerView.Adapter<NewsSubAdapter.MyViewHolder> {

    private ArrayList<NewsVo> newsVos;
    private Context context;

    public NewsSubAdapter(ArrayList<NewsVo> newsVos, Context context) {
        this.newsVos = newsVos;
        this.context = context;
    }

    /**
     * 用来创建布局
     * <p>
     * onCreateViewHolder参数含义：
     * resource：需要加载布局文件的id，意思是需要将这个布局文件中加载到Activity中来操作。
     * root：需要附加到resource资源文件的根控件，什么意思呢，就是inflate()会返回一个View对象，如果第三个参数attachToRoot为true，就将这个root作为根对象返回，否则仅仅将这个root对象的LayoutParams属性附加到resource对象的根布局对象上，也就是布局文件resource的最外层的View上，比如是一个LinearLayout或者其它的Layout对象。
     * attachToRoot：是否将root附加到布局文件的根视图上
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_sub_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    /**
     * 赋值
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //直接赋值
        holder.news_title.setText(newsVos.get(position).getTitle());
        holder.news_content.setText(newsVos.get(position).getSummary());
        holder.news_time.setText(newsVos.get(position).getStamp());

        //获取新闻图标（预览图），本身是个链接，javaForGet请求网络端，获取流，通过流转化成Bitmap
        ImageLoadUtil imageLoadUtil = new ImageLoadUtil();
        imageLoadUtil.getImageBitmap(newsVos.get(position).getIcon(), holder.news_icon);
    }

    @Override
    public int getItemCount() {
        return newsVos.size();
    }

    //用来管理缓存控件
    public class MyViewHolder extends XRecyclerView.ViewHolder {

        ImageView news_icon;
        TextView news_title, news_content, news_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            //findViewById
            news_icon = (ImageView) itemView.findViewById(R.id.news_icon);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_content = (TextView) itemView.findViewById(R.id.news_content);
            news_time = (TextView) itemView.findViewById(R.id.news_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转详情页面
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("VO", newsVos.get(getPosition() - 1));
                    context.startActivity(intent);
                }
            });
        }
    }
}
