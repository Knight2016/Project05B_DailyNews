package com.project.dailynewsb.dailynews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.util.WaitingDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Base类
 * Created by macbook on 2016/11/29.
 */

public abstract class BaseActivity extends FragmentActivity {

    private WaitingDialog waitingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayout() != 0) {
            setContentView(getLayout());

            //绑定注解
            ButterKnife.bind(this);

            initData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //获取布局
    public abstract int getLayout();
    //获取数据
    public abstract void initData();


    //开启等待层
    public void showWaitingProgress() {

        if (waitingDialog == null) {
            waitingDialog = new WaitingDialog(this, R.style.MyWaitingDialogStyle);
        }

        waitingDialog.startAnimation();
        waitingDialog.show();
    }

    //关闭等待层
    public void hideWaitingProgress() {

        if (waitingDialog != null) {
            waitingDialog.stopAnimation();
            waitingDialog.cancel();
        }
    }

    /**
     * 两秒退出程序
     */
    long lastTime;

    public boolean isExit() {

        if (System.currentTimeMillis() - lastTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }
}
