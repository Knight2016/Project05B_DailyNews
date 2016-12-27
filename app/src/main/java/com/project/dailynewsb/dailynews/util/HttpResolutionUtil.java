package com.project.dailynewsb.dailynews.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析网络端数据
 * Created by macbook on 2016/12/6.
 */

public class HttpResolutionUtil {

    public static <T> List<T> resolution(String jsonStr, Class<T> classOfT, Context context, String requestFrom) {

        Gson gson = new Gson();
        List<T> resultList = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getInt("status") == 0 && jsonObject.has("data")) {
                //请求成功(1、包含数组|2、包含对象)
                if (jsonObject.get("data").toString().startsWith("[")) {
                    //数组
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        T result = gson.fromJson(jsonArray.getJSONObject(i).toString(), classOfT);
                        resultList.add(result);
                    }
                } else {
                    //对象
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    T result = gson.fromJson(jsonObject1.toString(), classOfT);
                    resultList.add(result);
                }

            } else {
                //请求异常
                Toast.makeText(context, "错误原因（" + requestFrom + ")：" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
