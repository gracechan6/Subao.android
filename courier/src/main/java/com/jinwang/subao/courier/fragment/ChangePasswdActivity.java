package com.jinwang.subao.courier.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinwang.subao.courier.R;
import com.jinwang.subao.courier.activity.SettingActivity;
import com.jinwang.subao.courier.config.UrlParam;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by dreamy on 2015/6/29.
 */
public class ChangePasswdActivity extends android.app.DialogFragment {
    private EditText oldPasswd;
    private EditText newPasswd;
    private EditText repeateNewPasswd;
    private Button btnSubmit;
    private Toolbar mToolBar;
    private TextView mTitle;
    private ImageButton imageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_change_password, null);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldPasswd=(EditText)view.findViewById(R.id.et_old_password);
        newPasswd = (EditText)view.findViewById(R.id.et_new_password);
        repeateNewPasswd = (EditText)view.findViewById(R.id.et_repeate_new_password);
        imageButton=(ImageButton)view.findViewById(R.id.close);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSubmit = (Button)view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户填写资料
                final String oldPassword = oldPasswd.getText().toString();
                final String newPassword = newPasswd.getText().toString();
                final String newPassword2 = repeateNewPasswd.getText().toString();
                //获取uuid与username
                final String savePassword=getArguments().getString("savePassword");
                final String mUuid=getArguments().getString("mUuid");
                if(oldPassword == null || oldPassword.toString().trim().length() == 0){
                    doOnDialogDone("1");
                    return;
                }
                if(!oldPassword.endsWith(savePassword)){
                    doOnDialogDone("2");
                    return;
                }
                if(newPassword == null || newPassword.toString().trim().length() == 0){
                    doOnDialogDone("3");
                    return;
                }
                if(newPassword2 == null || newPassword2.toString().trim().length() == 0){
                    doOnDialogDone("4");
                    return;
                }
                if(!newPassword.endsWith(newPassword2)){
                    doOnDialogDone("5");
                    return;
                }
                doSubmit(newPassword,mUuid);
            }
        });
    }
    private void doOnDialogDone(String num){
        SettingActivity sa=(SettingActivity)getActivity();
        sa.onDialogDone(getTag(),true,num);
    }
    private void doSubmit(String newPassword,String mUuid){
        Log.i(getClass().getSimpleName(), "show pasword " + newPassword + " muuid " + mUuid);
        String url= UrlParam.CHANGEPASSWORD_URL;
        RequestParams params=new RequestParams();
        params.put("Newpassword",newPassword);
        params.put("Muuid", mUuid);
        AsyncHttpClient client=new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String message = null;
                try {
                    message = new String(responseBody, "GBK");
                    JSONObject jo = new JSONObject(message);
                    Log.i(getClass().getSimpleName(), "send success " + jo.toString());
                    if (jo.getBoolean("success")) {
                        Log.i(getClass().getSimpleName(), "show change success ");
                        doOnDialogDone("6");
                        dismiss();
                    } else {
                        Log.i(getClass().getSimpleName(), "show change error ");
                        doOnDialogDone("0");
                        oldPasswd.setText("");
                        newPasswd.setText("");
                        repeateNewPasswd.setText("");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(getClass().getSimpleName(), "show change fail ");
            }
        });
    }
}
