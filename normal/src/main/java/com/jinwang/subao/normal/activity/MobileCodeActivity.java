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
import com.jinwang.subao.normal.entity.UserInfo;
import com.jinwang.subao.normal.utils.PreferenceUtils;
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
public class MobileCodeActivity extends BaseActivity {
    private EditText mobileCode;
    private TextView mTitle;
    private Toolbar mToolBar;
    private Button mtnCode;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i(getClass().getSimpleName(), "进入MobileCodeActivity页面");
        //设置页面
        setContentView(R.layout.activity_vercode);
        //发送验证码
//        sendCode();
        //设置头部文件
        initToolBar();
        //绑定验证码
        mobileCode=(EditText)findViewById(R.id.et_mobileCode);
        //绑定验证按钮
        mtnCode =(Button)findViewById(R.id.btn_Code);
        mtnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }
}
