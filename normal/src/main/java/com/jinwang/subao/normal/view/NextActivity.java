package com.jinwang.subao.normal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.jinwang.subao.normal.MainActivity;
import com.jinwang.subao.normal.R;
import com.jinwang.subao.normal.WebviewActivity;
import com.jinwang.subao.normal.activity.SettingActivity;

import java.util.Map;

public class NextActivity extends WebviewActivity {

    private Intent intent;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理回传数据mActionBar.setNavigationIcon(R.drawable.icon_back);
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getSimpleName(), "进入回传数据处理模块 ");
        if (requestCode == NEXT_ACTIVITY_REQUEST_CODE && null != data) {
            String transferData = data.getStringExtra(EXTRA_TRANSFER_DATA);
            //有回传数据
            if (null != transferData) {

                Log.i(getClass().getSimpleName(), "Transfer callback data: " + transferData);

                //传递参数
//                mWebview.loadUrl("javascript:jwGobal.complete(" + pageNum + ")");
                mWebview.loadUrl("javascript:jwGobal.transferData(" + transferData + ")");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (pageNum==4){
            intent = new Intent().setClass(NextActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.setNavigationIcon(R.drawable.icon_back);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNum == 4) {
                    intent = new Intent().setClass(NextActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });
        if(this.pageNum==6 || this.pageNum==7){
            mActionBar.inflateMenu(R.menu.menu_settings);
            mActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Log.i(getClass().getSimpleName(), "jwGobal.complete() ");
                    mWebview.loadUrl("javascript:jwGobal.complete()");
//                    mWebview.loadUrl("javascript:jwGobal.getUserName("+"111"+")");
//                    mWebview.loadUrl("file:///android_asset/mPuTong/userInfoQrcode.html?content=C0C2AD52-DD1D-C101-0FF0-BF01B4A1956A&size=100");
                    return true;
                }
            });
        }
    }
}
