package com.project.dailynewsb.dailynews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.entity.CommentVo;
import com.project.dailynewsb.dailynews.util.CircleImageView;
import com.project.dailynewsb.dailynews.util.ImageLoadUtil;

import java.util.ArrayList;

/**
 * Created by macbook on 2016/12/8.
 */

public class CommentAdapter extends XRecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private ArrayList<CommentVo> commentVos;

    public CommentAdapter(ArrayList<CommentVo> commentVos) {
        this.commentVos = commentVos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.commnet_item_name.setText(commentVos.get(position).getUid());
        holder.commnet_item_content.setText(commentVos.get(position).getContent());
        holder.commnet_item_time.setText(commentVos.get(position).getStamp());
        //获取新闻图标（预览图），本身是个链接，javaForGet请求网络端，获取流，通过流转化成Bitmap
        ImageLoadUtil imageLoadUtil = new ImageLoadUtil();
        imageLoadUtil.getImageBitmap(commentVos.get(position).getPortrait(), holder.commnet_item_icon);

    }

    @Override
    public int getItemCount() {
        return commentVos.size();
    }

    public class MyViewHolder extends XRecyclerView.ViewHolder {

        CircleImageView commnet_item_icon;
        TextView commnet_item_name, commnet_item_content, commnet_item_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            commnet_item_icon = (CircleImageView) itemView.findViewById(R.id.commnet_item_icon);
            commnet_item_name = (TextView) itemView.findViewById(R.id.commnet_item_name);
            commnet_item_content = (TextView) itemView.findViewById(R.id.commnet_item_content);
            commnet_item_time = (TextView) itemView.findViewById(R.id.commnet_item_time);
        }
    }
}
