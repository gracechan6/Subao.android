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

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.config.UrlParam;
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
public class CodePasswdActivity extends BaseActivity {
    private TextView mPhone;
    private EditText mCode;
    private EditText mPassword;
    private EditText mPasswordS;
    private Button mSure;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_getpassword);
        initToolBar();
        final String loginPhone=getIntent().getStringExtra("mPhone");
        //绑定电话号码
        mPhone=(TextView)findViewById(R.id.lfg_mphone);
        mPhone.setText(loginPhone);
        //绑定验证码
        mCode=(EditText)findViewById(R.id.lfg_code);
        //绑定新密码
        mPassword=(EditText)findViewById(R.id.lfg_newpassword);
        //绑定确认密码
        mPasswordS=(EditText)findViewById(R.id.lfg_newpasswords);
        //绑定确认按键
        mSure=(Button)findViewById(R.id.lfg_sure);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Code=mCode.getText().toString();
                final String password=mPassword.getText().toString();
                final String passwords=mPasswordS.getText().toString();
                if(Code==null || Code.toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password==null || password.toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwords==null || passwords.toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(),"确认密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwords.endsWith(password)){
                    Toast.makeText(getApplicationContext(),"两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetPassword(loginPhone, Code,password);
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
        mTitle.setText(getString(R.string.forget_get_password));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void doGetPassword(String loginPhone, final String Code,String password){
        String url=UrlParam.GETPWDSECOND_URL;
        RequestParams params=new RequestParams();
        params.put("Mobilephone",loginPhone);
        params.put("Identify",Code);
        params.put("Newpassword",password);
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String message=new String(responseBody,"GBK");
                    JSONObject jo=new JSONObject(message);
                    if(jo.getBoolean("success")){
                        Intent intent=new Intent(CodePasswdActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(),"修改新密码失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),"网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
