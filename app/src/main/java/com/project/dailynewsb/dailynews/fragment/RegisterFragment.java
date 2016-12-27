package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.activity.MainActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.entity.RegisterVo;
import com.project.dailynewsb.dailynews.util.HttpRequestUtil;
import com.project.dailynewsb.dailynews.util.HttpResolutionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbook on 2016/12/9.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText login_et_username, login_et_password, login_et_email;
    private Button login_btn;
    private RegisterVo registerVo;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, null);
        login_et_username = (EditText) view.findViewById(R.id.login_et_username);
        login_et_password = (EditText) view.findViewById(R.id.login_et_password);
        login_et_email = (EditText) view.findViewById(R.id.login_et_email);
        login_btn = (Button) view.findViewById(R.id.login_btn);

        //设置监听
        login_btn.setOnClickListener(this);

        return view;
    }


    /**
     * 注册:ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
     */
    public void registerFun() {

        mainActivity.showWaitingProgress();

        new Thread(new Runnable() {
            @Override
            public void run() {

                //数据请求
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("ver", "0"));
                nameValuePairs.add(new BasicNameValuePair("uid", login_et_username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("email", login_et_email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("pwd", login_et_password.getText().toString()));
                String result = HttpRequestUtil.request(AllRequestUrl.register, nameValuePairs);
                Log.e("TAL", "注册请求：" + result);

                //数据解析
                registerVo = HttpResolutionUtil.resolution(result, RegisterVo.class, getContext(), "注册接口").get(0);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    /**
     * 处理请求结果
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mainActivity.hideWaitingProgress();
            if (registerVo.getResult() == 0) {
                //注册成功
                mainActivity.changeFragment(6, "登录");
            }
            Toast.makeText(getContext(), registerVo.getExplain(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login_btn) {
            //注册
            if (login_et_username.getText().toString().equals("")) {
                Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (login_et_password.getText().toString().equals("")) {
                Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (login_et_email.getText().toString().equals("")) {
                Toast.makeText(getContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
                return;
            }

            registerFun();
        }
    }
}
