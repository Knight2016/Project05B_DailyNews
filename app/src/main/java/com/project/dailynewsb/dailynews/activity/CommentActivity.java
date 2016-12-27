package com.project.dailynewsb.dailynews.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.adapter.CommentAdapter;
import com.project.dailynewsb.dailynews.base.BaseActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.entity.CommentVo;
import com.project.dailynewsb.dailynews.entity.NewsVo;
import com.project.dailynewsb.dailynews.util.HttpRequestUtil;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.project.dailynewsb.dailynews.R.id.comment_xrv;
import static com.project.dailynewsb.dailynews.R.id.title_name;

/**
 * Created by macbook on 2016/12/8.
 */

public class CommentActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    @Bind(title_name)
    TextView titleName;
    @Bind(comment_xrv)
    XRecyclerView commentXrv;
    @Bind(R.id.title_home)
    ImageView titleHome;
    //标题头
    private NewsVo newsVo;
    private int lastCid = 0;//用于上拉加载更多
    private CommentAdapter adapter;
    private ArrayList<CommentVo> commentVos;

    @Override
    public int getLayout() {
        return R.layout.activity_comment;
    }

    @Override
    public void initData() {

        newsVo = (NewsVo) getIntent().getSerializableExtra("VO");

        // # 1 同一个TextView改变局部字体外形
        //title_name.setText(Html.fromHtml("<font color='#FF0000'>" + newsVo.getTitle() + "</font>" + " 的评论"));

        // # 2 同一个TextView改变局部字体外形
        SpannableString spannableString = new SpannableString(newsVo.getTitle() + " 的评论");
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.CommentTitleStyle), 0, newsVo.getTitle().length(), 0);
        titleName.setText(spannableString);
        titleHome.setImageResource(R.drawable.ic_arrow_back);

        initXRecyclerView();
        getCommentList(true);

    }

    /**
     * 初始化XRecyclerView
     */
    public void initXRecyclerView() {

        commentVos = new ArrayList<>();
        adapter = new CommentAdapter(commentVos);
        commentXrv.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentXrv.setLayoutManager(layoutManager);
        commentXrv.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        commentXrv.setLoadingListener(this);
    }

    /**
     * 获取评论数据：ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
     */
    public void getCommentList(final boolean isRefresh) {

        showWaitingProgress();

        if (isRefresh) {
            commentVos.clear();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("ver", "0"));
                pairs.add(new BasicNameValuePair("nid", String.valueOf(newsVo.getNid())));
                pairs.add(new BasicNameValuePair("type", "0"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                pairs.add(new BasicNameValuePair("stamp", simpleDateFormat.format(new Date())));
                if (isRefresh) {
                    pairs.add(new BasicNameValuePair("cid", "0"));
                    pairs.add(new BasicNameValuePair("dir", "1"));
                } else {
                    pairs.add(new BasicNameValuePair("cid", String.valueOf(lastCid)));
                    pairs.add(new BasicNameValuePair("dir", "2"));
                }
                pairs.add(new BasicNameValuePair("cnt", "20"));
                String result = HttpRequestUtil.request(AllRequestUrl.conmentList, pairs);
                Log.e("TAL", "评论列表：" + result);

                //数据解析
                commentVos.addAll(HttpResolutionUtil.resolution(result, CommentVo.class, CommentActivity.this, "评论接口"));
                lastCid = commentVos.get(commentVos.size() - 1).getCid();
                handler.sendEmptyMessage(1);

            }
        }).start();
    }

    /**
     * 更新界面
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideWaitingProgress();
            commentXrv.refreshComplete();
            commentXrv.loadMoreComplete();
            commentXrv.setPullRefreshEnabled(true);
            commentXrv.setLoadingMoreEnabled(true);
            adapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onRefresh() {

        getCommentList(true);
        commentXrv.setPullRefreshEnabled(false);
        commentXrv.setLoadingMoreEnabled(false);

    }

    @Override
    public void onLoadMore() {

        getCommentList(false);
        commentXrv.setPullRefreshEnabled(false);
        commentXrv.setLoadingMoreEnabled(false);

    }

    @OnClick(R.id.title_home)
    public void onClick() {
        finish();
    }
}
