package com.project.dailynewsb.dailynews.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by macbook on 2016/12/1.
 */

public class MyBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> tArrayList;

    public MyBaseAdapter(ArrayList<T> tArrayList) {
        this.tArrayList = tArrayList;
    }

    @Override
    public int getCount() {
        return tArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
