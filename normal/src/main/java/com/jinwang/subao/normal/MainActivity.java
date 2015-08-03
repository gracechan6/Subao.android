package com.jinwang.subao.normal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jinwang.subao.normal.activity.SettingActivity;
import com.jinwang.subao.normal.chat.ChatMsgListActivity;
import com.jinwangmobile.ui.base.activity.BaseActivity;
import com.jinwang.subao.normal.chat.ddpush.ChatService;
import com.jinwang.subao.normal.R;


/**
 * Created by dreamy on 2015/6/25.
 */
public class MainActivity extends BaseActivity {
    private Toolbar mToolBar;
    private TextView mTitle;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindDDPushStart();
        initToolBar();
//        initView();
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
        mTitle.setText(getString(R.string.app_name));
        mToolBar.addView(mTitle, lp);
        mToolBar.setNavigationIcon(R.drawable.icon_message);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatMsgListActivity.class);
                startActivity(intent);
            }
        });

        mToolBar.inflateMenu(R.menu.menu_settings);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
    private void bindDDPushStart(){
        Intent startSrv = new Intent(this, ChatService.class);
        startSrv.putExtra("CMD", "RESET");
        this.startService(startSrv);
    }
//    private void initView(){
//    }
}
