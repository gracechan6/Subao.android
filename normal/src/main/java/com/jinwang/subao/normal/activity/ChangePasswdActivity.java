package com.jinwang.subao.normal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinwang.subao.normal.R;
import com.jinwangmobile.ui.base.activity.BaseActivity;

/**
 * Created by dreamy on 2015/6/29.
 */
public class ChangePasswdActivity extends BaseActivity{
    private EditText oldPasswd;
    private EditText newPasswd;
    private EditText repeateNewPasswd;
    private Button btnSubmit;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPasswd = (EditText)findViewById(R.id.et_old_password);
        newPasswd = (EditText)findViewById(R.id.et_new_password);
        repeateNewPasswd = (EditText)findViewById(R.id.et_repeate_new_password);

        final String oldPassword = oldPasswd.getText().toString();
        final String newPassword = newPasswd.getText().toString();
        final String newPassword2 = repeateNewPasswd.getText().toString();

        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPassword == null || oldPassword.toString().trim().length() == 0){
                    Toast.makeText(ChangePasswdActivity.this, "原密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                if(newPassword == null || newPassword.toString().trim().length() == 0){
                    Toast.makeText(ChangePasswdActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                if(newPassword2 == null || newPassword2.toString().trim().length() == 0){
                    Toast.makeText(ChangePasswdActivity.this, "请输入确认密码！", Toast.LENGTH_SHORT).show();
                }
                if(!newPassword.endsWith(newPassword2)){
                    Toast.makeText(ChangePasswdActivity.this, "新密码两次输入不一致！", Toast.LENGTH_SHORT).show();
                }
                doSubmit();
            }
        });
    }

    private void doSubmit(){

    }
}
