package com.jinwang.subao.normal;

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

import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.config.AppParams;
import com.jinwang.subao.normal.config.ConstantConfig;
import com.jinwang.subao.normal.config.UrlParam;
import com.jinwang.subao.normal.dialog.LoadingDialog;
import com.jinwang.subao.normal.entity.RegisterResult;
import com.jinwang.subao.normal.utils.PreferenceUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by dreamy on 2015/6/24.
 */
public class ResAuthActivity extends BaseActivity {
    private TextView usrNumber;
    private EditText authEdit;
    private Button btnConfirm;
    private LoadingDialog loadingDialog;
    private String user;
    private String password;
    private Toolbar mToolBar;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initToolBar();
        loadingDialog = new LoadingDialog(this);
        Bundle bundle = getIntent().getExtras();
         user = bundle.getString("UserName");
        String authCode = bundle.getString("AuthCode");
        password = bundle.getString("Password");
        usrNumber = (TextView) findViewById(R.id.tv_username);
        usrNumber.setText(user);

        authEdit = (EditText) findViewById(R.id.et_code);
        authEdit.setText(authCode);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doSubmitAuthCode();
            }
        });
    }

    private void doSubmitAuthCode(){
        final String authCode = authEdit.getText().toString();
        //合法性检查
        if(authCode == null || authCode.toString().trim().length() == 0){
            Toast.makeText(getApplication(), "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String userName = (String) usrNumber.getText();
        RequestParams params = new RequestParams();
        params.put(AppParams.USERNAME, userName);
        params.put(AppParams.CHECKCODE, authCode);

        String url = UrlParam.VERCODE_URL;
        loadingDialog.show();
        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                loadingDialog.dismiss();
                try {
                    JSONObject jo = new JSONObject(new String(bytes));
                    if (jo.getBoolean("success")) {
                        String returnData = jo.getString("returnData");
                        returnData = returnData.replace("[", "");
                        returnData = returnData.replace("]", "");
                        RegisterResult result = AbsJSONUtils.defaultInstance().JSON2Object(returnData, RegisterResult.class);
                        if (result.getReturnFlag().endsWith("1")) {
                            doRegister(password);
//                            Intent intent = new Intent();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("UserName", userName);
//                            intent.putExtras(bundle);
//                            intent.setClass(ResAuthActivity.this, SetPasswdActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(getApplication(), "验证码验证失败", Toast.LENGTH_SHORT).show();
                        }
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
                Toast.makeText(getApplication(), "验证码提交失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void doRegister(final String password){
//        loadingDialog.show();

        RequestParams params = new RequestParams();
        params.put(AppParams.USERNAME, user);
        params.put(AppParams.PASSWORD, password);
        params.put(AppParams.ROLE, ConstantConfig.ROLE);

        String url = UrlParam.REGISTER_URL;

        AsyncHttpClient client = ((SubaoApplication)getApplication()).getSharedHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                loadingDialog.dismiss();
                try {
                    JSONObject jo = new JSONObject(new String(bytes));
                    if (jo.getBoolean("success")) {
                        String returnData = jo.getString("returnData");
                        returnData = returnData.replace("[", "");
                        returnData = returnData.replace("]", "");
                        RegisterResult result = AbsJSONUtils.defaultInstance().JSON2Object(returnData, RegisterResult.class);
                        if (result.getReturnFlag().endsWith("1")) {
                            PreferenceUtils.saveUserInfo(ResAuthActivity.this, user, password);
                            Intent intent = new Intent();
                            intent.setClass(ResAuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {

                    }
                }catch(IOException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                loadingDialog.dismiss();
                Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();
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
        mTitle.setText(getString(R.string.action_input_code));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResAuthActivity.this.finish();
            }
        });
    }
}
