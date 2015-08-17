package com.jinwang.subao.courier.activity;

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

import com.jinwang.subao.courier.R;
import com.jinwang.subao.courier.config.UrlParam;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by dreamy on 2015/6/23.
 */
public class ForgetPasswdActivity extends BaseActivity {
    private EditText mobilePhone;
    private Button getPasswordBtn;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password);
        initToolBar();
        String loginPhone=getIntent().getStringExtra("mPhone");
        Log.i(getClass().getSimpleName(),"传过来的号码"+loginPhone);
        //电话号码绑定
        mobilePhone=(EditText)findViewById(R.id.fp_username);
        mobilePhone.setText(loginPhone);
        //找回按钮绑定
        getPasswordBtn=(Button)findViewById(R.id.fp_Button);
        getPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找回密码第一步发送验证码
                doPwdFirst();
            }
        });
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
        mTitle.setText(getString(R.string.forget_password));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void doPwdFirst(){
        final String mPhone=mobilePhone.getText().toString();
        String url= UrlParam.GETPWDFIRST_URL;
        RequestParams params=new RequestParams();
        params.put("Mobilephone",mPhone);
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String message=new String(responseBody,"GBK");
                    JSONObject jo=new JSONObject(message);
                    Log.i(getClass().getSimpleName(),"获取验证码"+jo.getBoolean("success"));
                    if(jo.getBoolean("success")){
                        Log.i(getClass().getSimpleName(),"获取成功"+jo.getBoolean("success"));
                        Intent intent=new Intent(ForgetPasswdActivity.this,CodePasswdActivity.class);
                        intent.putExtra("mPhone",mPhone);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "网络连接错误请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
