package com.jinwang.subao.normal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.jinwang.subao.normal.activity.SettingActivity;
import com.jinwang.subao.normal.chat.ChatMsgListActivity;
import com.jinwang.subao.normal.chat.ddpush.ChatService;
import com.jinwangmobile.ui.base.activity.BaseWebviewActivity;

import org.json.JSONArray;


/**
 * Created by dreamy on 2015/6/25.
 */
public class MainActivity extends WebviewActivity {
//    private Toolbar mToolBar;
    private SurfaceHolder sfh;
    private TextView mTitle;
    JSONArray allObject=new JSONArray();
    private  String rootUrl="file:///android_asset/mPuTong/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i(getClass().getSimpleName(), "onCreate");
//        WebSettings settings = mWebview.getSettings();
//        settings.setDomStorageEnabled(true);
        bindDDPushStart();
        initToolBar();
        setUrlPath(rootUrl + "index.html");
        webviewLoadData();
    }
    protected void initToolBar()
    {
        //设置自定义标题
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        mTitle = new TextView(this);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mTitle.setText(getString(R.string.app_name));
        mActionBar.addView(mTitle, lp);
        mActionBar.setNavigationIcon(R.drawable.icon_message);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatMsgListActivity.class);
                startActivity(intent);
            }
        });

        mActionBar.inflateMenu(R.menu.menu_settings);
        mActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEXT_ACTIVITY_REQUEST_CODE && null != data) {
            String transferData = data.getStringExtra(EXTRA_TRANSFER_DATA);

            //有回传数据
            if (null != transferData) {

                Log.i(getClass().getSimpleName(), "Transfer callback data: " + transferData);

                //传递参数
                mWebview.loadUrl("javascript:jwGobal.transferData('" + transferData + "')");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
