package com.jinwang.subao.normal.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.config.ActionConfig;
import com.jinwang.subao.normal.config.ServerConfig;
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.MainActivity;
import com.jinwang.subao.normal.dialog.LoadingDialog;
import com.jinwang.subao.normal.utils.PreferenceUtils;
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
public class LoginActivity extends BaseActivity {
    private Button btnLogin;
    private TextView forgetPasswd;
    private TextView register;
    private EditText userNameEdit;
    private EditText passwordEdit;
    private LoadingDialog loadingDialog;
    private View loginView;
    private View registerView;
    private View forgetView;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        loadingDialog = new LoadingDialog(this);
        loginView = findViewById(R.id.login);
        registerView = findViewById(R.id.register);
        forgetView = findViewById(R.id.forget);
        //登陆名绑定
        userNameEdit = (EditText) findViewById(R.id.et_username);
        //登陆密码绑定
        passwordEdit = (EditText) findViewById(R.id.et_password);

        SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_PRIVATE);
        final String saveUserName = sp.getString(PreferenceUtils.PREFERENCE_USERNAME, "");
        if (saveUserName!=null){
            userNameEdit.setText(saveUserName);
            passwordEdit.setText(sp.getString(PreferenceUtils.PREFERENCE_PASSWORD, ""));
        }
        //登陆按钮绑定
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getSimpleName(), "btnLogin");
                doLogin();
            }
        });
        //忘记密码按钮绑定
        forgetPasswd = (TextView) findViewById(R.id.tv_forget_passwd);;
        forgetPasswd.setVisibility(View.VISIBLE);
        forgetPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgetPasswdActivity.class);
                final String userName = userNameEdit.getText().toString();
                if(userName!=null || userName!=""){
                    intent.putExtra("mPhone",userName);
                }else {
                    intent.putExtra("mPhone","");
                }
                startActivity(intent);
            }
        });

//        register.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
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
        mTitle.setText(getString(R.string.app_login));
        mToolBar.addView(mTitle, lp);
//        mToolBar.setNavigationIcon(R.drawable.icon_back);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(LoginActivity.this, ChatMsgListActivity.class);
////                startActivity(intent);
//            }
//        });
        //导航栏添加注册按钮
        mToolBar.inflateMenu(R.menu.menu_register);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.i(getClass().getSimpleName(), "注册");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    private void doLogin(){
        loadingDialog.show();
        final String userName = userNameEdit.getText().toString();
        final String password = passwordEdit.getText().toString();

        //合法性检查
        if(userName == null || userName.toString().trim().length() == 0){
            Log.i(getClass().getSimpleName(), "用户名不能为空");
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }
        if(password == null || password.toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("Mobilephone", userName);
        params.put("Password", password);
        params.put("OperationType", "A");
        String url = UrlParam.LOGIN_URL;
        Log.i(getClass().getSimpleName(), "Login params " + url+params.toString());
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(getClass().getSimpleName(), "Login Success");
                String errMsg= null;
                try {
                    errMsg = new String(responseBody,"GBK");
                    Log.i(getClass().getSimpleName(), "Login Success" + errMsg);
                    JSONObject jo = new JSONObject(errMsg);
                    if(jo.getBoolean("success")){
                        PreferenceUtils.saveUserInfo(LoginActivity.this, userName, password,jo.getJSONArray("returnData").getJSONObject(0).getString("mUuid"));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        loadingDialog.dismiss();
                    }else{
                        loadingDialog.dismiss();
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
                Log.i(getClass().getSimpleName(), "login Failure");
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "访问连接错误", Toast.LENGTH_SHORT).show();
            }
        });
//        RequestParams params = new RequestParams();
//        params.put(AppParams.USERNAME, userName);
//        params.put(AppParams.PASSWORD, password);
//        params.put(AppParams.OP_TYPE, ConstantConfig.OS);
//        params.put(AppParams.MUUID, "");
//        String url = UrlParam.LOGIN_URL;
//        AsyncHttpClientApi.get(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                loadingDialog.dismiss();
//
//                try {
//                JSONObject jo = new JSONObject(new String(bytes));
//                if (jo.getBoolean("success")) {
//                    String returnData = jo.getString("returnData");
//                    returnData = returnData.replace("[", "");
//                    returnData = returnData.replace("]", "");
//                    LoginResult result = AbsJSONUtils.defaultInstance().JSON2Object(returnData, LoginResult.class);
//                    if (result.getReturnFlag().endsWith("1")) {
//                        PreferenceUtils.saveUserInfo(LoginActivity.this, userName, password);
//                        Intent intent = new Intent();
//                        intent.setClass(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//
//                        byte[] fullByte = AppUtils.gbk2utf8(result.getErrMsg());
//                        String str = new String(fullByte, "UTF-8");
//                        ShowToast.showCentreToast(LoginActivity.this, str);
//                    }
//                }else {
//                    byte[] fullByte = AppUtils.gbk2utf8(jo.getString("errMsg"));
//                    String str = new String(fullByte, "UTF-8");
//                    ShowToast.showCentreToast(LoginActivity.this, str);
//                }
//            }catch(IOException e){
//                e.printStackTrace();
//            }catch(JSONException e){
//                e.printStackTrace();
//            }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                loadingDialog.dismiss();
//                ShowToast.showCentreToast(LoginActivity.this, "登录失败！");
//            }
//        });
//
    }
}
