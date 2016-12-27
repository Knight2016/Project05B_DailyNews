package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.MainActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.config.Config;
import com.project.dailynewsb.dailynews.entity.RegisterVo;
import com.project.dailynewsb.dailynews.entity.UserVo;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.project.dailynewsb.dailynews.config.Config.userVo;

/**
 * Created by macbook on 2016/12/9.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextView login_register;
    private MainActivity mainActivity;
    private Button login_btn;
    private EditText login_et_username, login_et_password;
    private RegisterVo registerVo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, null);
        login_register = (TextView) view.findViewById(R.id.login_register);
        login_btn = (Button) view.findViewById(R.id.login_btn);
        login_et_username = (EditText) view.findViewById(R.id.login_et_username);
        login_et_password = (EditText) view.findViewById(R.id.login_et_password);
        login_register.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login_register) {
            //跳转注册界面
            mainActivity.changeFragment(7, "注册");
        } else if (v.getId() == R.id.login_btn) {
            //请求登录接口

            if (login_et_username.getText().toString().equals("")) {
                Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (login_et_password.getText().toString().equals("")) {
                Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            loginFun();
        }
    }

    /**
     * # 1 登录
     */
    public void loginFun() {
        Log.e("TAL", "# 1 登录");

        RequestParams params = new RequestParams();
        params.addBodyParameter("ver", "0");
        params.addBodyParameter("uid", login_et_username.getText().toString());
        params.addBodyParameter("pwd", login_et_password.getText().toString());
        params.addBodyParameter("device", "0");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, AllRequestUrl.login, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                mainActivity.showWaitingProgress();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //数据解析
                if (HttpResolutionUtil.resolution(responseInfo.result, RegisterVo.class, getContext(), "登录接口") == null || HttpResolutionUtil.resolution(responseInfo.result, RegisterVo.class, getContext(), "登录接口").size() == 0) {
                    mainActivity.hideWaitingProgress();
                    return;
                }
                registerVo = HttpResolutionUtil.resolution(responseInfo.result, RegisterVo.class, getContext(), "登录接口").get(0);
                //登录成功
                Config.token = registerVo.getToken();//以备以后使用
                if (registerVo.getResult() == 0) {
                    //获取个人中心数据
                    personCenter();
                } else {
                    mainActivity.hideWaitingProgress();
                }
                Toast.makeText(getContext(), registerVo.getExplain(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    /**
     * # 2 个人中心
     */
    public void personCenter() {
        Log.e("TAL", "# 2 个人中心");

        RequestParams params = new RequestParams();
        params.addBodyParameter("ver", "0");
        params.addBodyParameter("imei", "0");
        params.addBodyParameter("token", registerVo.getToken());
        new HttpUtils().send(HttpRequest.HttpMethod.POST, AllRequestUrl.personCenter, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //数据解析
                Config.userVo = HttpResolutionUtil.resolution(responseInfo.result, UserVo.class, getContext(), "个人中心接口").get(0);
                // 更新全局已登录变量
                Config.isLogin = true;

                //获取头像Bitmap
                getIcon();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    /**
     * # 3 获取头像
     */
    public void getIcon() {
        Log.e("TAL", "# 3 获取头像");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(userVo.getPortrait());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == 200) {
                        Config.userBitmap = BitmapFactory.decodeStream(connection.getInputStream());
                        handler.sendEmptyMessage(1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 头像获取成功，消除等待层
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mainActivity.hideWaitingProgress();

            if (mainActivity.flag == 6) {
                //从评论界面跳过来
                mainActivity.finish();
            } else {
                //获取个人中心成功(回到右侧边栏界面，更新界面头像和用户名)
                mainActivity.rightFragment.updateMsg();
                mainActivity.changeFragment(1, "新闻", 1);
            }
        }
    };
}
