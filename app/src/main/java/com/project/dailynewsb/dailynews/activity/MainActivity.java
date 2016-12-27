package com.project.dailynewsb.dailynews.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.base.BaseActivity;
import com.project.dailynewsb.dailynews.fragment.CollectionFragment;
import com.project.dailynewsb.dailynews.fragment.LeftFragment;
import com.project.dailynewsb.dailynews.fragment.LoginFragment;
import com.project.dailynewsb.dailynews.fragment.NewsFragment;
import com.project.dailynewsb.dailynews.fragment.RegisterFragment;
import com.project.dailynewsb.dailynews.fragment.RightFragment;
import com.project.dailynewsb.dailynews.util.slidingmenu.SlidingMenu;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    //标题头信息
    private SlidingMenu slidingMenu;

    @Bind(R.id.title_name)
    public TextView title_name;

    //Fragment
    private LeftFragment leftFragment;
    public RightFragment rightFragment;
    private NewsFragment newsFragment;
    private CollectionFragment collectionFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    //切换
    private FragmentManager fm;
    private FragmentTransaction ft;

    //用来分辨从哪个入口进入此Activity
    public int flag = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

        //初始化SlidingMenu
        initSlidingMenu();

        //初始化Fragment
        initFragment();

        //根据参数跳转相应界面
        flag = getIntent().getIntExtra("FLAG", 0);

    }

    /**
     * 初始化SlidingMenu
     */
    private void initSlidingMenu() {

        slidingMenu = new SlidingMenu(this);
        //设置为左右两边菜单栏
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置全屏范围都可以打开菜单栏
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置菜单栏的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置菜单栏与类的关联：当前类显示的为菜单栏的中间界面
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //设置左菜单栏样式
        slidingMenu.setMenu(R.layout.menu_left);
        //设置右菜单栏样式
        slidingMenu.setSecondaryMenu(R.layout.menu_right);

        //初始化左侧菜单
        leftFragment = new LeftFragment();
        rightFragment = new RightFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.menu_left, leftFragment);
        ft.replace(R.id.menu_right, rightFragment);
        ft.commit();
    }

    /**
     * 展示左侧菜单栏
     */
    public void showLeftMenu(View v) {
        slidingMenu.showMenu();
    }

    /**
     * 初始化Fragment
     */
    public void initFragment() {

        newsFragment = new NewsFragment();
        collectionFragment = new CollectionFragment();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

    }

    /**
     * 切换Fragment
     */
    public void changeFragment(int position, String title, int... flag) {

        if (flag.length == 0) {
            slidingMenu.showContent();//展示主界面
        } else {
            if (flag[0] == 1) {
                slidingMenu.showSecondaryMenu();//展示右菜单
            } else if (flag[0] == 2) {
                slidingMenu.showMenu();//展示左菜单
            }
        }

        title_name.setText(title);//更换标题头

        switch (position) {
            case 1:
                //想切换新闻
                ft = fm.beginTransaction();
                ft.replace(R.id.main_contain, newsFragment);
                ft.commit();

                break;

            case 2:
                //想切换收藏
                ft = fm.beginTransaction();
                ft.replace(R.id.main_contain, collectionFragment);
                ft.commit();

                break;

            case 6:
                //想切换登录
                ft = fm.beginTransaction();
                ft.replace(R.id.main_contain, loginFragment);
                ft.commit();

                break;

            case 7:
                //想切换注册
                ft = fm.beginTransaction();
                ft.replace(R.id.main_contain, registerFragment);
                //ft.addToBackStack("registerFragment");
                ft.commit();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit()) {
            super.onBackPressed();
        }
        //fm.popBackStack();
    }
}
