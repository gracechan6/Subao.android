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
import com.jinwang.subao.courier.dialog.LoadingDialog;
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
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private EditText userNameEdit;
    private EditText passwordEdit;
    private EditText idNumberEdit;
    private EditText workNumberEdit;
    private Button btnRegister;
    private LoadingDialog loadingDialog;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        loadingDialog = new LoadingDialog(this);
        //绑定注册名
        userNameEdit = (EditText) findViewById(R.id.et_username);
        //绑定注册密码
        passwordEdit = (EditText) findViewById(R.id.et_password);
        //绑定身份证号码
        idNumberEdit=(EditText)findViewById(R.id.et_idnumber);
        //绑定工作证号码
        workNumberEdit=(EditText)findViewById(R.id.et_worknumber);
        //绑定注册按钮
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
        final String idNumber=idNumberEdit.getText().toString();
        final String workNumber=workNumberEdit.getText().toString();
        //合法性检查
        if(userName == null || userName.toString().trim().length() == 0){
            Log.i("RegisterActivity", "userName");
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password == null || password.toString().trim().length() == 0){
            Log.i("RegisterActivity", "password");
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(idNumber == null || idNumber.toString().trim().length() == 0){
            Log.i("RegisterActivity", "password");
            Toast.makeText(getApplicationContext(), "身份证号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(workNumber == null || workNumber.toString().trim().length() == 0){
            Log.i("RegisterActivity", "password");
            Toast.makeText(getApplicationContext(), "工作证号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        sendCode(userName, password,idNumber,workNumber);
//        RequestParams params = new RequestParams();
//        params.put(AppParams.USERNAME, userName);
//        String url = UrlParam.GET_VERCODE_URL;
//        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        /*client.get(url, params, new AsyncHttpResponseHandler() {
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
        });*/
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
    /**
     *发送验证码
     */
    public void sendCode(final String userName,final String password,final String idNumber,final String workNumber){
        String url= UrlParam.GET_VERCODE_URL;
        RequestParams params = new RequestParams();
        params.put("Mobilephone", userName);
        Log.i(getClass().getSimpleName(), "userInfo Mobilephone " + userName);
        if(userName!=null || userName!=""){
            AsyncHttpClient client=new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String errMsg=new String(responseBody,"GBK");
                        JSONObject jo = new JSONObject(errMsg);
                        Log.i(getClass().getSimpleName(), "Login Success" + jo.toString());
                        if(jo.getBoolean("success")){
                            //发送雁阵码成功打开新的activity
                            Intent intent = new Intent(RegisterActivity.this, MobileCodeActivity.class);
                            Log.i(getClass().getSimpleName(),"userName"+userName+",password"+password+",idNumber"+idNumber+",workNumber"+workNumber);
                            intent.putExtra("userName", userName);
                            intent.putExtra("password", password);
                            intent.putExtra("idNumber", idNumber);
                            intent.putExtra("workNumber", workNumber);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), jo.getString("errMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i(getClass().getSimpleName(), "login Failure");
                    Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        }else {

        }
    }

}
