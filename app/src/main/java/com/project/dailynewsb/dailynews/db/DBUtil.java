package com.project.dailynewsb.dailynews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.project.dailynewsb.dailynews.entity.NewsVo;

import java.util.ArrayList;

/**
 * Created by macbook on 2016/12/7.
 */

public class DBUtil {

    private Context context;
    private MyOpenHelper openHelper;
    private SQLiteDatabase database;

    public DBUtil(Context context) {
        this.context = context;
        openHelper = new MyOpenHelper(context, "FAV", null, 1);
        database = openHelper.getWritableDatabase();
    }

    /**
     * 收藏到数据库中
     */
    public void addFav(int newsId, String jsonStr) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", newsId);
        contentValues.put("jsonStr", jsonStr);
        database.insert(MyOpenHelper.TABLE_NAME, null, contentValues);
        Log.e("TAL", "您插入了一条数据");

    }


    /**
     * 从该数据库中移除
     */
    public void delFav(int newsId) {

        database.delete(MyOpenHelper.TABLE_NAME, " id = ?", new String[]{String.valueOf(newsId)});

    }

    /**
     * 从数据库中查询所有
     */
    public ArrayList<NewsVo> queryAllFav() {

        ArrayList<NewsVo> newsVos = new ArrayList<>();
        Gson gson = new Gson();

        String sql = "select * from " + MyOpenHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String jsonStr = cursor.getString(cursor.getColumnIndex("jsonStr"));
            newsVos.add(gson.fromJson(jsonStr, NewsVo.class));
        }

        return newsVos;
    }

    /**
     * 判断某个新闻是否被收藏
     */
    public boolean isFav(int newsId) {

        String sql = "select * from " + MyOpenHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("id")) == newsId) {
                //此新闻已经收藏
                return true;
            }
        }
        return false;
    }
}
