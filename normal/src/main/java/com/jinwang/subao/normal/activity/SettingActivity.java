package com.jinwang.subao.normal.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
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
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwang.subao.normal.utils.PreferenceUtils;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwangmobile.ui.base.view.CircleImageView;
import com.jinwang.subao.normal.entity.UserInfo;
import com.jinwang.subao.normal.view.ActionSheet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    private UserInfo mUserInfo;
    public static final String PHOTO_TYPE = "PHOTO_TYPE";

    //选择照片后实现方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(getClass().getSimpleName(), "Request ok with code[" + requestCode + "]");
        if (CROP_REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK)
        {
            String bitmap = data.getStringExtra("BITMAP_DATA");
            Log.i(getClass().getSimpleName(), "bitmap "+bitmap);
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
        setHeadPhoto();
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
        mTitle.setText(getString(R.string.action_settings));
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
        //照片按钮绑定
        headImage = (CircleImageView) findViewById(R.id.headPhoto);
        headImage.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                //显示图片按键
                showActionSheet();
            }
        });
        //设置名字按钮绑定
        userInfo = (RelativeLayout) findViewById(R.id.ll_name);
        userInfo.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        //关于甬宝按钮绑定
        relAbout = (RelativeLayout) findViewById(R.id.rel_about);
        relAbout.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "正在研发中", Toast.LENGTH_SHORT).show();
            }
        });
        //用户协议按钮绑定
        relAgreement = (RelativeLayout) findViewById(R.id.rel_agreement);
        relAgreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "正在研发中", Toast.LENGTH_SHORT).show();
            }
        });
        //意见反馈按钮绑定
        relSuggestion = (RelativeLayout) findViewById(R.id.rel_suggestion);
        relSuggestion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "正在研发中", Toast.LENGTH_SHORT).show();
            }
        });
        //修改密码按钮绑定
        changePasswd = (RelativeLayout) findViewById(R.id.rel_change_passwd);
        changePasswd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswd();
            }
        });
    }

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
//        setHeadImage();
        setHeadPhoto();
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

    /**
     * 显示图片按键
     */
    public void showActionSheet() {
        ActionSheet.createBuilder(SettingActivity.this, this.getSupportFragmentManager())
                .setCancelButtonTitle(getString(R.string.cancel))
                .setOtherButtonTitles( getString(R.string.take_photo), getString(R.string.from_photo))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    /**
     * 其他按钮点击监听事件
     * @param actionSheet  固定写法实例化固定类
     * @param index   第几个按钮
     */
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
    private void uploadPhoto(final String bitmap)
    {
        Log.i(getClass().getSimpleName(), "enter uploadPhoto" + bitmap);
        //保存头像到临时文件，上传完成后删除
        final File file = new File(bitmap);
        try
        {
            //获取存储在文件中的登录名
            SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
            final String saveUserName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
            Log.i(getClass().getSimpleName(), "Login Name" + saveUserName);
            //获取请求路径
            String url= UrlParam.SENDPICTURE_URL;
            //设置请求参数
            RequestParams params = new RequestParams();
            params.put("fileName",saveUserName+".png");
            params.put("id","imageUpload");
            params.put("mainId",saveUserName);
            params.put("moduleId", "moduleIdIdenty");
            params.put("pic", file);
            Log.i(getClass().getSimpleName(), "Login params " + url + params.toString());
            AsyncHttpClient client=new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String message=new String(responseBody,"GBK");
                        JSONObject jo = new JSONObject(message);
                        Log.i(getClass().getSimpleName(), "sendPic Success" + jo.toString());
                        if(jo.getBoolean("success")){
                            Toast.makeText(getApplicationContext(), "上传图片成功", Toast.LENGTH_SHORT).show();

                            final String headFilePath =ConstantConfig.TEMP_FILE_PATH + File.separator + saveUserName + ".png";
                            Log.i(getClass().getSimpleName(), "change the path" + headFilePath);
                            file.renameTo(new File(headFilePath));
                            file.delete();
                            //上传本地存在问题调用更换头像方法
                            setHeadPhoto();
                        }else {
                            Log.i(getClass().getSimpleName(), "sendPic error" + jo.toString());
                            Toast.makeText(getApplicationContext(), "上传图片失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i(getClass().getSimpleName(), "sendPic bad");
                    Toast.makeText(getApplicationContext(), "上传图片出错", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e)
        {
            Log.e(getClass().getSimpleName(), e.toString());
        }
    }

    /**
     * 设置头像
     */
    public void setHeadPhoto()
    {
        //获取存储在文件中的登录名
        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        final String saveUserName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
        final String mUuid=sp.getString(PreferenceUtils.PREFERENCE_MUUID,"");
        final String headFilePath =ConstantConfig.TEMP_FILE_PATH + File.separator + saveUserName + ".png";
        Log.i(getClass().getSimpleName(), "get the path" + headFilePath);
        final File headF = new File(headFilePath);
        Log.i(getClass().getSimpleName(), "enter setHeadPhoto " + headFilePath);
        //绘图
        Bitmap head=null;
        Log.i(getClass().getSimpleName(), "headF.exists "+headF.exists());
        if(headF.exists()){
            //解决加载大图片内存溢出问题
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            head = BitmapFactory.decodeFile(headFilePath, options);
            headImage.setImageBitmap(head);
        }else{
            String url=UrlParam.GETPICTURE_URL;
            RequestParams params = new RequestParams();
            params.put("Muuid",mUuid);
            AsyncHttpClient client=new AsyncHttpClient();
            client.post(url,params,new JsonHttpResponseHandler("GBK"){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if(response.getBoolean("success")){
                            Log.i(getClass().getSimpleName(), "show getPictureUrl "+ response.getString("pathLoad"));
                            //绘图
                            Bitmap head=null;
                            BitmapFactory.Options options=new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            head = BitmapFactory.decodeFile(response.getString("pathLoad"), options);
                            headImage.setImageBitmap(head);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    public void changePasswd(){
        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        final String savePassword = sp.getString(PreferenceUtils.PREFERENCE_PASSWORD, "");
        final String mUuid=sp.getString(PreferenceUtils.PREFERENCE_MUUID,"");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev=getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment  = new ChangePasswdActivity();
        Bundle args = new Bundle();
        args.putString("savePassword", savePassword);
        args.putString("mUuid", mUuid);
        newFragment.setArguments(args);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        newFragment.show(ft, "dialog");
    }

    public void onDialogDone(String tag, boolean cancelled, CharSequence message) {
        if(cancelled){
            switch (message.toString()){
                case "0":
                    Toast.makeText(getApplicationContext(), "修改密码失败！", Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    Toast.makeText(getApplicationContext(), "原密码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(getApplicationContext(), "原密码错误！", Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    Toast.makeText(getApplicationContext(), "新密码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                case "4":
                    Toast.makeText(getApplicationContext(), "请输入确认密码！", Toast.LENGTH_SHORT).show();
                    break;
                case "5":
                    Toast.makeText(getApplicationContext(), "新密码两次输入不一致！", Toast.LENGTH_SHORT).show();
                    break;
                case "6":
                    Toast.makeText(getApplicationContext(), "修改密码成功！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
