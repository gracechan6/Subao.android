package com.jinwang.subao.normal.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.ResAuthActivity;
import com.jinwang.subao.normal.SubaoApplication;
import com.jinwang.subao.normal.config.AppParams;
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwang.subao.normal.dialog.LoadingDialog;
import com.jinwang.subao.normal.entity.RegisterResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dreamy on 2015/6/23.
 */
public class RegisterActivity extends BaseActivity {
    private EditText userNameEdit;
    private EditText passwordEdit;
    private Button btnRegister;
    private LoadingDialog loadingDialog;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        loadingDialog = new LoadingDialog(this);
        userNameEdit = (EditText) findViewById(R.id.et_username);
        passwordEdit = (EditText) findViewById(R.id.et_password);
        btnRegister = (Button) findViewById(R.id.btn_login);
        btnRegister.setText(getString(R.string.action_register));
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegisterStart();
            }
        });
    }

    private void doRegisterStart(){
        final String userName = userNameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        //合法性检查
        if(userName == null || userName.toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password == null || password.toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put(AppParams.USERNAME, userName);

        String url = UrlParam.GET_VERCODE_URL;

        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                loadingDialog.dismiss();
                try {
                    JSONObject jo = new JSONObject(new String(responseBody));
                    if (jo.getBoolean("success")) {
                        String returnData = jo.getString("returnData");
                        returnData = returnData.replace("[", "");
                        returnData = returnData.replace("]", "");
                        RegisterResult result = AbsJSONUtils.defaultInstance().JSON2Object(returnData, RegisterResult.class);
                        if (result.getReturnFlag().endsWith("1")) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("UserName", userName);
                            bundle.putString("Password", password);
                            bundle.putString("AuthCode", result.getCHECKCODE());
                            intent.putExtras(bundle);
                            intent.setClass(RegisterActivity.this, ResAuthActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "注册提交失败", Toast.LENGTH_SHORT).show();
                finish();
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
        mTitle.setText(getString(R.string.action_register));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

}
