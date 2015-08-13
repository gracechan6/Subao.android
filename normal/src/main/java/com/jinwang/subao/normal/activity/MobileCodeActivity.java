package com.jinwang.subao.normal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.normal.MainActivity;
import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwang.subao.normal.dialog.LoadingDialog;
import com.jinwang.subao.normal.entity.UserInfo;
import com.jinwang.subao.normal.utils.PreferenceUtils;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.Policy;

/**
 * Created by dreamy on 2015/6/23.
 * 验证码验证
 */
public class MobileCodeActivity extends BaseActivity {
    private EditText mobileCode;
    private TextView mTitle;
    private Toolbar mToolBar;
    private Button mtnCode;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "进入MobileCodeActivity页面");
        //设置页面
        setContentView(R.layout.activity_vercode);
        //设置头部文件
        initToolBar();
        //绑定验证按钮
        mtnCode =(Button)findViewById(R.id.btn_Code);
        mtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证码验证
                doCheckCode();
            }
        });
    }
    /**
     * 设置头部文件
     */
    protected void initToolBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        //设置自定义标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setText(getString(R.string.action_mobile_code));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * 验证码验证
     */
    public void doCheckCode(){
//        loadingDialog.show();
        //绑定验证码
        mobileCode=(EditText)findViewById(R.id.et_mobileCode);
        final String Code = mobileCode.getText().toString();
        if(Code == null || Code.toString().trim().length() == 0){
            Log.i(getClass().getSimpleName(), "验证码不能为空");
            Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
//            loadingDialog.dismiss();
            return;
        }
        //验证码验证
        String url=UrlParam.VERCODE_URL;
        RequestParams params = new RequestParams();
        params.put("Mobilephone", getIntent().getStringExtra("userName"));
        params.put("Checkcode", Code);
        Log.i(getClass().getSimpleName(), "验证码验证连接 " + url + params.toString());
        AsyncHttpClient client= new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = null;
                try {
                    message = new String(responseBody, "GBK");
                    JSONObject jo = new JSONObject(message);
                    Log.i(getClass().getSimpleName(), "验证码验证连接 " + jo.toString());
                    if (jo.getBoolean("success")) {
                        //调用注册方法
                        doRegisier();
                    } else {
                        Log.i(getClass().getSimpleName(), "验证码错误 " + jo.getString("errMsg"));
                        Toast.makeText(getApplicationContext(), jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                        mobileCode.setText("");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 注册
     */
    public void doRegisier(){
        AsyncHttpClient client= new AsyncHttpClient();
        String url=UrlParam.REGISTER_URL;
        RequestParams params = new RequestParams();
        params.put("Mobilephone",getIntent().getStringExtra("userName"));
        params.put("Password", getIntent().getStringExtra("password"));
        params.put("Role","Group0004");
        Log.i(getClass().getSimpleName(), "注册连接地址 " + url + params.toString());
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String message = new String(responseBody,"GBK");
                    JSONObject jo = new JSONObject(message);
                    Log.i(getClass().getSimpleName(), "注册 " + jo.toString());
                    if(jo.getBoolean("success")){
                        Intent intent = new Intent(MobileCodeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "注册成功请登录", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
