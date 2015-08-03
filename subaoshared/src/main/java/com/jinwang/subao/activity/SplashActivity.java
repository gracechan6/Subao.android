package com.jinwang.subao.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jinwang.subao.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化应用，创建数据库，启动消息推送等



        //初始化完成后，启动主界面
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Thread.sleep(1000);
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
