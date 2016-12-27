package com.project.dailynewsb.dailynews.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.dailynewsb.dailynews.R;
import com.project.dailynewsb.dailynews.base.BaseActivity;
import com.project.dailynewsb.dailynews.config.AllRequestUrl;
import com.project.dailynewsb.dailynews.config.Config;
import com.project.dailynewsb.dailynews.util.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

import static com.project.dailynewsb.dailynews.R.id.title_home;
import static com.project.dailynewsb.dailynews.R.id.title_name;

/**
 * Created by macbook on 2016/12/13.
 */

public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(title_home)
    ImageView titleHome;//返回键
    @Bind(title_name)
    TextView titleName;//标题头
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.integral)
    TextView integral;
    @Bind(R.id.person_center_icon)
    CircleImageView personCenterIcon;
    @Bind(R.id.person_center_lin)
    LinearLayout personCenterLin;

    //头像上传
    private PopupWindow popupWindow;
    private LinearLayout photo_take, photo_sel;
    private File file;//拍照结果文件

    @Override
    public int getLayout() {
        return R.layout.activity_person_center;
    }

    @Override
    public void initData() {

        titleHome.setImageResource(R.drawable.ic_arrow_back);
        titleName.setText(Config.userVo.getUid() + " 的个人中心");
        name.setText(Config.userVo.getUid());
        integral.setText("积分数：" + Config.userVo.getIntegration());
        personCenterIcon.setImageBitmap(Config.userBitmap);

        //初始化PopupWindow
        initPopupWidow();

    }

    /**
     * 初始化PopupWindow
     */
    public void initPopupWidow() {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_icon_select, null);
        photo_take = (LinearLayout) view.findViewById(R.id.photo_take);
        photo_sel = (LinearLayout) view.findViewById(R.id.photo_sel);
        photo_take.setOnClickListener(this);
        photo_sel.setOnClickListener(this);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.MyPopupWindowStyle);

    }

    /**
     * 退出登录
     */
    public void exitLogin(View v) {

        Config.isLogin = false;
        Config.userBitmap = null;
        Config.userVo = null;
        Config.token = "";

        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.photo_take:
                //拍照
                // # 1 准备拍照存储路径
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/a.jpg");
                Log.e("TAL", "拍照路径：" + file.getAbsolutePath());

                // # 2 开始拍照
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file.getAbsoluteFile()));
                startActivityForResult(intent, 1);

                popupWindow.dismiss();

                break;

            case R.id.photo_sel:
                //从相册选取
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_PICK);
                intent1.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 2);

                popupWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            //拍照成功
            uploadImage(file);//上传头像

        } else if (requestCode == 2) {
            //图库选取图片成功
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            uploadImage(new File(picturePath));//上传头像
        }
    }

    /**
     * 上传头像：token=用户令牌& portrait =头像
     */
    public void uploadImage(final File iconFile) {

        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("token", Config.token);
        requestParams.addBodyParameter("portrait", iconFile);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, AllRequestUrl.uploadImage, requestParams, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitingProgress();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.e("TAL", "头像上传成功：" + responseInfo.result);
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    if (jsonObject.getInt("status") == 0) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getInt("result") == 0) {
                            //上传成功(1、改变界面头像|1、更新全局头像)
                            Bitmap bitmap = BitmapFactory.decodeFile(iconFile.getAbsolutePath());
                            personCenterIcon.setImageBitmap(bitmap);
                            Config.userBitmap = bitmap;
                            Toast.makeText(PersonCenterActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitingProgress();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("TAL", "头像上传失败：" + msg);
                hideWaitingProgress();
            }
        });
    }

    @OnClick(R.id.person_center_icon)
    public void onClick() {
        //弹出上传头像方式
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAtLocation(personCenterLin, Gravity.BOTTOM, 0, 0);
        }
    }
}
