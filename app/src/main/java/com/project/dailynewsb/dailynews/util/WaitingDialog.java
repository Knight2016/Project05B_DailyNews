package com.project.dailynewsb.dailynews.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.project.dailynewsb.dailynews.R;

/**
 * Created by macbook on 2016/12/3.
 */

public class WaitingDialog extends Dialog {

    private ImageView waiting_iv;
    private Animation animation;

    public WaitingDialog(Context context) {
        super(context);
        initView(context);
    }

    public WaitingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected WaitingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public void initView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_waitting, null);
        waiting_iv = (ImageView) view.findViewById(R.id.waiting_iv);
        animation = AnimationUtils.loadAnimation(context, R.anim.anim_waiting);
        animation.setInterpolator(new LinearInterpolator());

        setContentView(view);
    }

    public void startAnimation() {
        waiting_iv.startAnimation(animation);
    }

    public void stopAnimation() {
        waiting_iv.clearAnimation();
    }
}
