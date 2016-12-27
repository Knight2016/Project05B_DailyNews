package com.project.dailynewsb.dailynews.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 用户获取网络图片
 */
public class ImageLoadUtil {

    private ImageView imageView;
    private Bitmap bitmap;

    public void getImageBitmap(final String imageUrl, final ImageView imageView) {

        this.imageView = imageView;

        Log.e("TAL", "1>>>" + imageUrl);

        //用java的get请求获取图片
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == 200) {
                        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                        ImageLoadUtil.this.bitmap = bitmap;
                        Log.e("TAL", "2>>>" + bitmap);
                        handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAL", "3>>>" + e.getMessage());
                }
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
            imageView.setImageBitmap(bitmap);
        }
    };
}
