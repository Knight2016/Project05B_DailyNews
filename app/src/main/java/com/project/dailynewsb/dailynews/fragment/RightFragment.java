package com.project.dailynewsb.dailynews.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.project.dailynewsb.dailynews.activity.PersonCenterActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.config.Config;
import com.project.dailynewsb.dailynews.util.CircleImageView;

import org.json.JSONObject;

import java.io.File;

import static com.project.dailynewsb.dailynews.config.Config.userVo;

/**
 * Created by macbook on 2016/12/9.
 */

public class RightFragment extends Fragment implements View.OnClickListener {

    private TextView textView_name;
    private MainActivity mainActivity;
    private CircleImageView imageView_photo;
    private TextView right_update;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_right, null);
        textView_name = (TextView) view.findViewById(R.id.textView_name);
        imageView_photo = (CircleImageView) view.findViewById(R.id.imageView_photo);
        right_update = (TextView) view.findViewById(R.id.right_update);

        //设置监听
        textView_name.setOnClickListener(this);
        right_update.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.textView_name) {
            if (Config.isLogin) {
                //已经登录（跳转个人中心）
                startActivity(new Intent(getActivity(), PersonCenterActivity.class));

            } else {
                //跳转登录
                mainActivity.changeFragment(6, "登录");
            }
        } else if (v.getId() == R.id.right_update) {
            //版本升级
            checkVersion();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMsg();
    }

    /**
     * 更新界面信息
     */
    public void updateMsg() {
        if (!Config.isLogin) {
            textView_name.setText("点击登录");
            imageView_photo.setImageResource(R.drawable.biz_pc_main_info_profile_avatar_bg_dark);
        } else {
            textView_name.setText(userVo.getUid());
            imageView_photo.setImageBitmap(Config.userBitmap);
        }
    }

    /**
     * 检测版本：imei=唯一识别号&pkg=包名&ver=版本
     */
    public void checkVersion() {

        mainActivity.showWaitingProgress();

        RequestParams params = new RequestParams();
        params.addBodyParameter("imei", "0");
        params.addBodyParameter("pkg", mainActivity.getPackageName());
        params.addBodyParameter("ver", "1");
        new HttpUtils().send(HttpRequest.HttpMethod.POST, AllRequestUrl.update, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("TAL", "版本请求：" + responseInfo.result);
                mainActivity.hideWaitingProgress();
                try {

                    JSONObject json = new JSONObject(responseInfo.result);
                    final String link = json.getString("link");

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("温馨提示");
                    builder.setMessage("发现新版本，是否更新？");
                    builder.setNegativeButton("不更新", null);
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downApk(link);
                        }
                    });
                    builder.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mainActivity.hideWaitingProgress();
            }
        });
    }

    /**
     * 下载apk
     */
    ProgressDialog progressDialog;

    public void downApk(String url) {

        url = "http://services.gradle.org/distributions/gradle-2.2-rc-2-bin.zip";//测试

        File fileDri = new File("/data/data/" + mainActivity.getPackageName() + "/apk/");
        fileDri.mkdirs();
        final File file = new File(fileDri, "a." + url.substring(url.lastIndexOf(".")));
        new HttpUtils().download(url, file.getAbsolutePath(), new RequestCallBack<File>() {

            @Override
            public void onStart() {
                super.onStart();
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("正在下载……");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {

                //调取安装
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);

            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);

                progressDialog.setProgress((int) current);
                progressDialog.setMax((int) total);

            }
        });
    }
}
