package com.jinwang.subao.normal.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.config.ActionConfig;
import com.jinwang.subao.normal.config.ConstantConfig;
import com.jinwang.subao.normal.config.ServerConfig;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwangmobile.ui.base.view.CircleImageView;
import com.jinwang.subao.normal.entity.UserInfo;
import com.jinwang.subao.normal.view.ActionSheet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

/**
 * Created by dreamy on 2015/6/29.
 */
public class SettingActivity extends BaseActivity implements ActionSheet.ActionSheetListener{
    private static int CROP_REQUEST_CODE = 10001;
    public static final int PHOTO_TYPE_0 = 0;
    public static final int PHOTO_TYPE_1 = 1;
    private CircleImageView headImage;
    private TextView userName;
    private RelativeLayout userInfo;
    private RelativeLayout relAbout;
    private RelativeLayout relAgreement;
    private RelativeLayout relSuggestion;
    private RelativeLayout changePasswd;
    private RelativeLayout relAddress;
    private RelativeLayout relUpdate;
    private Button btnExit;
    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView mID;
    private AsyncHttpClient client;

    private UserInfo mUserInfo;
    public static final String PHOTO_TYPE = "PHOTO_TYPE";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(getClass().getSimpleName(), "Request ok with code[" + requestCode + "]");
        if (CROP_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK)
        {
            String bitmap = data.getStringExtra("BITMAP_DATA");
            Log.i(getClass().getSimpleName(), "bitmap"+bitmap);
            if (null != bitmap)
            {
                uploadPhoto(bitmap);
            }
            else
            {
//                Toast.makeText(application, "", Toast.LENGTH_SHORT);
            }
        }
    }
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolBar();
        initView();
//        initUserInfo();
    }

    protected void initToolBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.toolbar);

        //设置自定义标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setText(getString(R.string.app_name));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        userName = (TextView) findViewById(R.id.useName);
        headImage = (CircleImageView) findViewById(R.id.headPhoto);
        headImage.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                showActionSheet();
            }
        });

        userInfo = (RelativeLayout) findViewById(R.id.ll_name);
        userInfo.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

        relAbout = (RelativeLayout) findViewById(R.id.rel_about);
        relAbout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relAgreement = (RelativeLayout) findViewById(R.id.rel_agreement);
        relAgreement.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        relSuggestion = (RelativeLayout) findViewById(R.id.rel_suggestion);
        relSuggestion.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        changePasswd = (RelativeLayout) findViewById(R.id.rel_change_passwd);
        changePasswd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        relAddress = (RelativeLayout) findViewById(R.id.rel_address);
//        relAddress.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        relUpdate = (RelativeLayout) findViewById(R.id.rel_update);
//        relUpdate.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        btnExit = (Button) findViewById(R.id.exit_btn);
//        btnExit.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    /**
     * 初始化用户信息
     */
    /**
     * 初始化用户信息
     */
    private void initUserInfo()
    {
//        if (null == mUserInfo)
//        {
//
//        }
        userName.setText(mUserInfo.getUserName());
        setHeadImage();
    }

    private void setHeadImage()
    {
//        //用户头像保存在头像目录下，以用户id为文件名
//        UserInfo userInfo = (UserInfo) application.getSharedData().get(LoginParams.USER_INFO);
//        final String headFilePath = ConstantConfig.HEADER_PATH + File.separator + userInfo.getUserId() + ".png";
//        final File headF = new File(headFilePath);
//
//        Bitmap head = null;
//
//        //去掉这部分，解决不同手机设置图像显示不一致问题。 modify by zengjp
//        //如果存在，直接使用，不存在，网络下载
//        if (headF.exists())
//        {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            head = BitmapFactory.decodeFile(headFilePath, options);
//
//            headImage.setImageBitmap(head);
//        }
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(SettingActivity.this, this.getSupportFragmentManager())
                .setCancelButtonTitle(getString(R.string.cancel))
                .setOtherButtonTitles( getString(R.string.take_photo), getString(R.string.from_photo))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        Log.i(getClass().getSimpleName(), "Click item index [" + index + "]");
        changePhoto(index);
    }

    /**
     * 修改头像
     * @param index 0 - 拍照， 1 - 相册
     */
    public void changePhoto(int index) {
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra(PHOTO_TYPE, index);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 上传头像到服务器以便从服务器获取图像
     * @param bitmap  头像本地存储路径
     */
    private void uploadPhoto(String bitmap)
    {
        Log.i(getClass().getSimpleName(), "enter uploadPhoto"+bitmap);
        //上传本地存在问题调用更换头像方法
        setHeadPhoto(bitmap);
        //保存头像到临时文件，上传完成后删除
        final File file = new File(bitmap);
        try
        {
            RequestParams params = new RequestParams();
            //设置请求参数
//            params.put("fileName",);
            params.put("pic", file);

            String url = ServerConfig.getAbsluteUrl(ActionConfig.ACTION_UPLOAD);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        } catch (IOException e)
        {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    /**
     * 设置头像
     * @param bitmap  头像路径
     */
    public void setHeadPhoto(String bitmap)
    {
        Log.i(getClass().getSimpleName(), "enter setHeadPhoto " + bitmap);
        //因为图片没有上传服务器直接从本地获取
        //创建图片
        File headF=new File(bitmap);
        //绘图
        Bitmap head=null;
        Log.i(getClass().getSimpleName(), "headF.exists "+headF.exists());
        if(headF.exists()){
            //解决加载大图片内存溢出问题
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            head = BitmapFactory.decodeFile(bitmap, options);
            headImage.setImageBitmap(head);
        }
    }
}
