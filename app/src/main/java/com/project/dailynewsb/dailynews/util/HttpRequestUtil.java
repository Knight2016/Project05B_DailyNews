package com.project.dailynewsb.dailynews.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * 网络请求工具类
 * Created by macbook on 2016/12/6.
 */

public class HttpRequestUtil {

    public static String request(String requestPath, List<NameValuePair> pairs) {

        try {

            HttpPost httpPost = new HttpPost(requestPath);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse != null) {
                return EntityUtils.toString(httpResponse.getEntity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
