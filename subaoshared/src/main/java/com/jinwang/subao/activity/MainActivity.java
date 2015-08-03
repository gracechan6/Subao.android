package com.jinwang.subao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jinwang.subao.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 显示登录界面
     * @param view
     */
    public void showLoginActivity(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

    /**
     * 显示忘记密码界面
     * @param view
     */
    public void showForgetPassActivity(View view)
    {

    }
}
