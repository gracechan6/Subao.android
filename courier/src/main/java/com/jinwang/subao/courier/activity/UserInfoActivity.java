package com.jinwang.subao.courier.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.courier.LoginActivity;
import com.jinwang.subao.courier.R;
import com.jinwang.subao.courier.config.ConstantConfig;
import com.jinwang.subao.courier.config.UrlParam;
import com.jinwang.subao.courier.utils.PreferenceUtils;
import com.jinwang.subao.courier.view.ActionSheet;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwangmobile.ui.base.view.CircleImageView;
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
 * Created by dreamy on 2015/7/9.
 *
 */
public class UserInfoActivity extends BaseActivity implements ActionSheet.ActionSheetListener{
    private static int CROP_REQUEST_CODE = 10001;
    public static final String PHOTO_TYPE = "PHOTO_TYPE";
    private Toolbar mToolBar;
    private TextView mTitle;
    private TextView nickName;
    private TextView yongbao;
    private TextView mobile;
    private RelativeLayout relEdit;
    private CircleImageView headImage;
    private WebView QR_Code;
    private Button loginOut;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
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
        mTitle.setText(getString(R.string.edit_user_info));
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
        //昵称
        nickName = (TextView) findViewById(R.id.nick);
        //速宝号
        yongbao = (TextView) findViewById(R.id.yongbao);
        //手机号
        mobile = (TextView) findViewById(R.id.mobile);
        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        final String userName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
        final String mUuid = sp.getString(PreferenceUtils.PREFERENCE_MUUID, "");
        mobile.setText(userName);
        //头像
        headImage=(CircleImageView)findViewById(R.id.headPhoto);
        //个人信息
        relEdit = (RelativeLayout) findViewById(R.id.photo);
        relEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionSheet();
            }
        });
        //注销登陆绑定
        loginOut=(Button)findViewById(R.id.exit_btn);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(UserInfoActivity.this,LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }
        });
        //二维码
        QR_Code=(WebView)findViewById(R.id.webView);
        Log.i(getClass().getSimpleName(), "二维码设置大小" + "file:///android_asset/mCourier/userInfoQrcode.html?content=" + mUuid + "&size=300px");
        QR_Code.setWebChromeClient(new WebChromeClient());
        QR_Code.setWebViewClient(new WebViewClient());

        WebSettings settings = QR_Code.getSettings();
        //设置支持Javascript的参数
        settings.setJavaScriptEnabled(true);
        //设置可以支持缩放
        settings.setSupportZoom(true);
        //设置出现缩放工具
        settings.setBuiltInZoomControls(true);
        //设置允许访问文件数据
        settings.setAllowFileAccess(true);
        if(Build.VERSION.SDK_INT > 15) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setCacheMode(-1);
        settings.setAppCacheEnabled(true);
        QR_Code.loadUrl("file:///android_asset/mCourier/userInfoQrcode.html?content=" + mUuid + "&size=98");
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(UserInfoActivity.this, this.getSupportFragmentManager())
                .setCancelButtonTitle(getString(R.string.cancel))
                .setOtherButtonTitles( getString(R.string.take_photo), getString(R.string.from_photo))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
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

                            final String headFilePath = ConstantConfig.TEMP_FILE_PATH + File.separator + saveUserName + ".png";
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


}
