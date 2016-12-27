package com.project.dailynewsb.dailynews.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 复习网络知识
 * Created by macbook on 2016/11/30.
 */

public class ReView extends Activity {


    private String requestUrlForGet = "http://118.244.212.82:9092/newsClient/news_sort?ver=0&imei=0";
    private String requestUrlForPost = "http://118.244.212.82:9092/newsClient/news_sort";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                androidPost();
            }
        }).start();


    }

    // # 1 Java原生Get请求：直接将请求参数拼接在请求路径后面，以?拼接，每个参数之间用&连接
    public void javaGet() {

        try {

            URL url = new URL(requestUrlForGet);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                //返回正常
                InputStream is = connection.getInputStream();

                int len;
                byte[] b = new byte[1024];
                StringBuffer sb = new StringBuffer();
                while ((len = is.read(b)) != -1) {
                    sb.append(new String(b, 0, len));
                }

                Log.e("TAL", "获取成功：" + sb.toString());

            } else {
                //返回异常
                Log.e("TAL", "获取失败，错误码：" + connection.getResponseCode());
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAL", "获取异常，异常原因：" + e.toString());
        }
    }

    // # 2 Java原生Post请求
    public void javaPost() {

        try {
            URL url = new URL(requestUrlForPost);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);//设置可传参：false
            connection.setDoInput(true);//设置可获取结果：true

            //Post传参：ver=0&imei=0
            OutputStream os = connection.getOutputStream();
            //String param = "ver=0&imei=0";
            String param = "ver=" + URLEncoder.encode("0", "UTF-8") + "&imei=" + URLEncoder.encode("0", "UTF-8");
            os.write(param.getBytes());

            if (connection.getResponseCode() == 200) {
                //返回正常
                InputStream is = connection.getInputStream();

                int len;
                byte[] b = new byte[1024];
                StringBuffer sb = new StringBuffer();
                while ((len = is.read(b)) != -1) {
                    sb.append(new String(b, 0, len));
                }

                Log.e("TAL", "获取成功：" + sb.toString());

            } else {
                //返回异常
                Log.e("TAL", "获取失败，错误码：" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAL", "获取异常，异常原因：" + e.toString());
        }

    }

    // # 3 Android Get请求
    public void androidGet() {

        try {

            HttpGet httpGet = new HttpGet(requestUrlForGet);
            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse != null) {

                HttpEntity httpEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(httpEntity);
                Log.e("TAL", "AndroidGet请求结果：" + result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // # 4 Android Post请求：ver=0&imei=0
    public void androidPost() {

        try {

            // # 1 准备参数
            HttpPost httpPost = new HttpPost(requestUrlForPost);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("ver", "0"));
            list.add(new BasicNameValuePair("imei", "0"));

            HttpEntity httpEntityPost = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(httpEntityPost);//往服务端扔参数

            // # 2 开启请求
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {

                // # 3 解析结果
                HttpEntity httpEntity = httpResponse.getEntity();//从服务端获取信息
                String result = EntityUtils.toString(httpEntity);
                Log.e("TAL", "AndroidPost请求结果：" + result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
