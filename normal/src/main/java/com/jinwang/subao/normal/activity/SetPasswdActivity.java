package com.jinwang.subao.normal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.core.dataparser.json.AbsJSONUtils;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.MainActivity;
import com.jinwang.subao.normal.SubaoApplication;
import com.jinwang.subao.normal.config.ConstantConfig;
import com.jinwang.subao.normal.config.AppParams;
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
public class SetPasswdActivity extends BaseActivity {
    private EditText passwdEdit;
    private EditText rePasswdEdit;
    private Button btnSubmit;
    private LoadingDialog loadingDialog;
    private String user;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_passwd);
        loadingDialog = new LoadingDialog(this);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("UserName");

        passwdEdit = (EditText) findViewById(R.id.et_password);
        rePasswdEdit = (EditText) findViewById(R.id.et_password_repeat);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doSubmit();
            }
        });
    }

    private void doSubmit(){
        loadingDialog.show();
        final String password = passwdEdit.getText().toString();
        final String rePassword = rePasswdEdit.getText().toString();

        //合法性检查
        if(password == null || password.toString().trim().length() == 0
            || rePassword == null || rePassword.toString().trim().length() == 0){
            Toast.makeText(getApplication(), "密码不能为空", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }
        if(!password.endsWith(rePassword)){
            Toast.makeText(getApplication(), "输入的密码不一致", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }

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
                            PreferenceUtils.saveUserInfo(SetPasswdActivity.this, user, password);
                            Intent intent = new Intent();
                            intent.setClass(SetPasswdActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "密码设置失败", Toast.LENGTH_SHORT).show();
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
}
