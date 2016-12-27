package com.project.dailynewsb.dailynews.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.base.BaseActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.config.Config;
import com.project.dailynewsb.dailynews.db.DBUtil;
import com.project.dailynewsb.dailynews.entity.NewsVo;
import com.project.dailynewsb.dailynews.entity.RegisterVo;
import com.project.dailynewsb.dailynews.util.HttpRequestUtil;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import static com.project.dailynewsb.dailynews.R.id.comment_input;
import static com.project.dailynewsb.dailynews.R.id.title_name;
import static com.project.dailynewsb.dailynews.R.id.webview_pro;
import static com.project.dailynewsb.dailynews.R.id.webview_wv;
import static com.project.dailynewsb.dailynews.config.AllRequestUrl.sendComment;

/**
 * Created by macbook on 2016/12/5.
 */

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

    @Bind(title_name)
    TextView titleName;
    @Bind(webview_pro)
    ProgressBar webviewPro;
    @Bind(webview_wv)
    WebView webviewWv;
    @Bind(comment_input)
    EditText commentInput;
    @Bind(R.id.title_home)
    ImageView titleHome;
    @Bind(R.id.actionShare)
    FloatingActionButton actionShare;
    @Bind(R.id.actionFav)
    FloatingActionButton actionFav;
    @Bind(R.id.comment_numicon)
    ImageView commentNumicon;
    @Bind(R.id.comment_num)
    TextView commentNum;
    @Bind(R.id.comment_send)
    TextView commentSend;

    private NewsVo newsVo;
    private RegisterVo registerVo;

    private boolean isFav = false;
    private DBUtil dbUtil;

    @Override
    public int getLayout() {
        return R.layout.activity_webview;
    }


    @Override
    public void initData() {

        newsVo = (NewsVo) getIntent().getSerializableExtra("VO");
        titleName.setText(newsVo.getTitle());
        initWebview(newsVo.getLink());
        titleHome.setImageResource(R.drawable.ic_arrow_back);

        //判断当前新闻是否已经被收藏
        dbUtil = new DBUtil(this);
        isFav = dbUtil.isFav(newsVo.getNid());
        if (isFav) {
            actionFav.setTitle("移除收藏");
        }

        //获取评论数量
        getCommentNum();
    }

    /**
     * 初始化WebView
     */
    public void initWebview(String url) {

        webviewWv.loadUrl(url);
        webviewWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webviewWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webviewPro.setProgress(newProgress);
                if (newProgress == 100) {
                    webviewPro.setVisibility(View.GONE);
                }
            }
        });

        WebSettings webSettings = webviewWv.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置自适应大小（需要写WebView的人在代码中设置）
        webSettings.setDisplayZoomControls(true); //设置出现WebView缩放按钮
        // 设置WebView属性，能够执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

    /**
     * 获取评论数量：ver=版本号& nid=新闻编号
     */
    public void getCommentNum() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                //数据请求
                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("ver", "0"));
                pairs.add(new BasicNameValuePair("nid", String.valueOf(newsVo.getNid())));
                String result = HttpRequestUtil.request(AllRequestUrl.conmentNum, pairs);
                Log.e("TAL", "评论>>>" + result);

                //数据解析
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 0) {

                        Message message = Message.obtain();
                        message.what = 1;
                        message.arg1 = jsonObject.getInt("data");
                        handler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新评论数量
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                commentNum.setText(String.valueOf(msg.arg1));
            } else if (msg.what == 2) {
                hideWaitingProgress();
                Toast.makeText(WebViewActivity.this, registerVo.getExplain(), Toast.LENGTH_SHORT).show();
                if (registerVo.getResult() == 0) {
                    //评论成功
                    commentInput.setText("");
                    getCommentNum();//重新获取评论
                }
            }
        }
    };

    /**
     * 发表评论：ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
     */
    public void sendComment() {

        showWaitingProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {

                //数据请求
                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("ver", "0"));
                pairs.add(new BasicNameValuePair("nid", String.valueOf(newsVo.getNid())));
                pairs.add(new BasicNameValuePair("token", Config.token));
                pairs.add(new BasicNameValuePair("imei", "0"));
                pairs.add(new BasicNameValuePair("ctx", commentInput.getText().toString()));
                String result = HttpRequestUtil.request(sendComment, pairs);
                Log.e("TAL", "发表评论结果>>>" + result);

                //数据解析
                registerVo = HttpResolutionUtil.resolution(result, RegisterVo.class, WebViewActivity.this, "登录接口").get(0);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址
        oks.setTitle(newsVo.getTitle());
        oks.setTitleUrl(newsVo.getLink());
        oks.setText(newsVo.getSummary());
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        oks.setImageUrl(newsVo.getIcon());
        oks.setUrl(newsVo.getLink()); //微信不绕过审核分享链接
        //oks.setFilePath("/sdcard/test-pic.jpg");  //filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
        oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl("http://mob.com");//QZone分享参数
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
        // 将快捷分享的操作结果将通过OneKeyShareCallback回调
        //oks.setCallback(new OneKeyShareCallback());
        // 去自定义不同平台的字段内容
        //oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        // 在九宫格设置自定义的图标
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.btn_back);
        String label = "测试";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(WebViewActivity.this, "你点击了测试", Toast.LENGTH_SHORT).show();
            }
        };
        oks.setCustomerLogo(logo, label, listener);

        // 为EditPage设置一个背景的View
        //oks.setEditPageBackground(getPage());
        // 隐藏九宫格中的新浪微博
        // oks.addHiddenPlatform(SinaWeibo.NAME);

        // String[] AVATARS = {
        //        "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
        //        "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
        //        "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
        //        "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
        //        "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
        //        "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
        // oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

        // 启动分享
        oks.show(context);
    }

    @OnClick({R.id.title_home, R.id.actionShare, R.id.actionFav, R.id.comment_numicon, R.id.comment_num, R.id.comment_send})
    public void onClick(View view) {

        Intent intent = new Intent(this, CommentActivity.class);

        switch (view.getId()) {
            case R.id.title_home:
                if (webviewWv.canGoBack()) {
                    webviewWv.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.actionShare:
                //分享
                showShare(this, null, true);
                break;
            case R.id.actionFav:
                //收藏

                if (isFav) {
                    //收藏→未收藏（移除）
                    dbUtil.delFav(newsVo.getNid());
                    actionFav.setTitle("收藏");
                } else {
                    Gson gson = new Gson();
                    //未收藏→收藏（添加）
                    dbUtil.addFav(newsVo.getNid(), gson.toJson(newsVo));
                    actionFav.setTitle("移除收藏");
                }
                isFav = !isFav;
                break;
            case R.id.comment_numicon:
                //跳转评论页面
                intent.putExtra("VO", newsVo);
                startActivity(intent);
                break;
            case R.id.comment_num:
                //跳转评论页面
                intent.putExtra("VO", newsVo);
                startActivity(intent);
                break;
            case R.id.comment_send:
                if (commentInput.getText().toString().length() == 0) {
                    Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Config.isLogin) {
                    //直接发表评论
                    sendComment();

                } else {
                    //未登录，去登陆
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent02 = new Intent(this, MainActivity.class);
                    intent02.putExtra("FLAG", 6);
                    startActivity(intent02);
                }
                break;
        }
    }
}
